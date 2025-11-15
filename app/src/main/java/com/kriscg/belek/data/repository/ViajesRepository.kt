package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.domain.Viaje
import io.github.jan.supabase.postgrest.from

class ViajesRepository {
    private val client = SupabaseConfig.client

    suspend fun crearViaje(viaje: Viaje): Result<Unit> {
        return try {
            client.from("viajes").insert(viaje)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getViajesByUsuario(usuarioId: String): Result<List<Viaje>> {
        return try {
            val viajes = client.from("viajes")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                    }
                }
                .decodeList<Viaje>()
            Result.success(viajes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUltimoViajeByUsuario(usuarioId: String): Result<Viaje?> {
        return try {
            val viajes = client.from("viajes")
                .select {
                    filter {
                        eq("usuario_id", usuarioId)
                    }
                }
                .decodeList<Viaje>()

            val ultimoViaje = viajes.maxByOrNull { it.createdAt ?: "" }
            Result.success(ultimoViaje)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getViajeById(id: Int): Result<Viaje> {
        return try {
            val viaje = client.from("viajes")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Viaje>()
            Result.success(viaje)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateViaje(id: Int, viaje: Viaje): Result<Unit> {
        return try {
            client.from("viajes")
                .update({
                    set("destino", viaje.destino)
                    set("tipo", viaje.tipo)
                    set("presupuesto", viaje.presupuesto)
                    set("intereses", viaje.intereses)
                }) {
                    filter {
                        eq("id", id)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteViaje(id: Int): Result<Unit> {
        return try {
            client.from("viajes")
                .delete {
                    filter {
                        eq("id", id)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}