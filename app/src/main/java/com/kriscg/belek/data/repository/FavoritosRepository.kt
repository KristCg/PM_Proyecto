
package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.domain.Favorito
import com.kriscg.belek.domain.Guardado
import io.github.jan.supabase.postgrest.from

class FavoritosRepository {
    private val client = SupabaseConfig.client

    suspend fun getFavoritosByUsuario(usuarioId: String): Result<List<Favorito>> {
        return try {
            val favoritos = client.from("favoritos")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                    }
                }
                .decodeList<Favorito>()
            Result.success(favoritos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isFavorito(usuarioId: String, lugarId: Int): Result<Boolean> {
        return try {
            val favoritos = client.from("favoritos")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("lugar_id", lugarId)
                    }
                }
                .decodeList<Favorito>()
            Result.success(favoritos.isNotEmpty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavorito(favorito: Favorito): Result<Unit> {
        return try {
            client.from("favoritos").insert(favorito)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorito(usuarioId: String, lugarId: Int): Result<Unit> {
        return try {
            client.from("favoritos")
                .delete {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("lugar_id", lugarId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGuardadosByUsuario(usuarioId: String): Result<List<Guardado>> {
        return try {
            val guardados = client.from("guardados")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                    }
                }
                .decodeList<Guardado>()
            Result.success(guardados)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isGuardado(usuarioId: String, lugarId: Int): Result<Boolean> {
        return try {
            val guardados = client.from("guardados")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("lugar_id", lugarId)
                    }
                }
                .decodeList<Guardado>()
            Result.success(guardados.isNotEmpty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addGuardado(guardado: Guardado): Result<Unit> {
        return try {
            client.from("guardados").insert(guardado)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeGuardado(usuarioId: String, lugarId: Int): Result<Unit> {
        return try {
            client.from("guardados")
                .delete {
                    filter {
                        eq("usuario_id", usuarioId)
                        eq("lugar_id", lugarId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}