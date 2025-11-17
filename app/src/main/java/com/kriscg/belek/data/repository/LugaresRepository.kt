package com.kriscg.belek.data.repository

import com.kriscg.belek.data.SupabaseConfig
import com.kriscg.belek.data.local.LocalDatabase
import com.kriscg.belek.data.local.toDomain
import com.kriscg.belek.data.local.toEntity
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import io.github.jan.supabase.postgrest.from

class LugaresRepository {
    private val client = SupabaseConfig.client
    private val lugarDao = LocalDatabase.db.lugarDao()


    suspend fun getAllLugares(): Result<List<Lugar>> {
        return try {
            val lugaresRemotos = client.from("lugares")
                .select()
                .decodeList<Lugar>()

            lugarDao.upsertAll(lugaresRemotos.map { it.toEntity() })

            Result.success(lugaresRemotos)
        } catch (e: Exception) {
            val locales = lugarDao.getAll()
            if (locales.isNotEmpty()) {
                Result.success(locales.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getLugarById(id: Int): Result<Lugar> {
        return try {
            val lugarRemoto = client.from("lugares")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Lugar>()

            lugarDao.upsert(lugarRemoto.toEntity())

            Result.success(lugarRemoto)
        } catch (e: Exception) {
            val local = lugarDao.getById(id)
            if (local != null) {
                Result.success(local.toDomain())
            } else {
                Result.failure(e)
            }
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