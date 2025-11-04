package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import io.github.jan.supabase.postgrest.from

class LugaresRepository {
    private val client = SupabaseConfig.client

    suspend fun getAllLugares(): Result<List<Lugar>> {
        return try {
            val lugares = client.from("lugares")
                .select()
                .decodeList<Lugar>()
            Result.success(lugares)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLugarById(id: Int): Result<Lugar> {
        return try {
            val lugar = client.from("lugares")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Lugar>()
            Result.success(lugar)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchLugares(query: String): Result<List<Lugar>> {
        return try {
            val lugares = client.from("lugares")
                .select {
                    filter {
                        ilike("nombre", "%$query%")
                    }
                }
                .decodeList<Lugar>()
            Result.success(lugares)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLugaresByTipo(tipo: String): Result<List<Lugar>> {
        return try {
            val lugares = client.from("lugares")
                .select {
                    filter {
                        eq("tipo", tipo)
                    }
                }
                .decodeList<Lugar>()
            Result.success(lugares)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getResenasByLugarId(lugarId: Int): Result<List<Resena>> {
        return try {
            val resenas = client.from("resenas")
                .select {
                    filter {
                        eq("lugar_id", lugarId)
                    }
                }
                .decodeList<Resena>()
            Result.success(resenas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addResena(resena: Resena): Result<Unit> {
        return try {
            client.from("resenas").insert(resena)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}