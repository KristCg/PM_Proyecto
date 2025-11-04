package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.domain.Usuario
import io.github.jan.supabase.postgrest.from

class UsuariosRepository {
    private val client = SupabaseConfig.client

    suspend fun getUsuarioById(id: String): Result<Usuario> {
        return try {
            val usuario = client.from("usuarios")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Usuario>()
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsuarioByUsername(username: String): Result<Usuario> {
        return try {
            val usuario = client.from("usuarios")
                .select {
                    filter {
                        eq("username", username)
                    }
                }
                .decodeSingle<Usuario>()
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUsername(userId: String, newUsername: String): Result<Unit> {
        return try {
            client.from("usuarios")
                .update({
                    set("username", newUsername)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEmail(userId: String, newEmail: String): Result<Unit> {
        return try {
            client.from("usuarios")
                .update({
                    set("email", newEmail)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDescripcion(userId: String, newDescripcion: String): Result<Unit> {
        return try {
            client.from("usuarios")
                .update({
                    set("descripcion", newDescripcion)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsuarios(): Result<List<Usuario>> {
        return try {
            val usuarios = client.from("usuarios")
                .select()
                .decodeList<Usuario>()
            Result.success(usuarios)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}