package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.domain.Usuario
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthRepository {
    private val client = SupabaseConfig.client

    suspend fun signUp(email: String, password: String, username: String): Result<Unit> {
        return try {
            println("DEBUG Auth: Intentando registrar usuario...")

            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = client.auth.currentUserOrNull()?.id
                ?: throw Exception("User ID not found")

            println("DEBUG Auth: Usuario registrado con ID: $userId")

            client.from("usuarios").insert(
                Usuario(
                    id = userId,
                    username = username,
                    email = email
                )
            )

            println("DEBUG Auth: Registro completo")
            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG Auth ERROR: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            println("DEBUG Auth: Intentando iniciar sesión...")

            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val session = client.auth.currentSessionOrNull()
            println("DEBUG Auth: Sesión obtenida - Token: ${session?.accessToken?.take(20)}...")
            println("DEBUG Auth: Usuario ID: ${session?.user?.id}")

            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG Auth ERROR: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            println("DEBUG Auth: Cerrando sesión...")
            client.auth.signOut()
            println("DEBUG Auth: Sesión cerrada")
            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG Auth ERROR al cerrar sesión: ${e.message}")
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        val userId = client.auth.currentUserOrNull()?.id
        println("DEBUG Auth: getCurrentUserId() = $userId")
        return userId
    }

    fun isUserLoggedIn(): Boolean {
        val session = client.auth.currentSessionOrNull()
        val user = client.auth.currentUserOrNull()

        println("DEBUG Auth: isUserLoggedIn()")
        println("  - Session: ${session != null}")
        println("  - User: ${user != null}")
        println("  - User ID: ${user?.id}")

        return user != null && session != null
    }

    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            client.auth.modifyUser {
                password = newPassword
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}