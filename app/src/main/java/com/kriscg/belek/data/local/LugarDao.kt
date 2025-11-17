package com.kriscg.belek.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LugarDao {

    @Query("SELECT * FROM lugares")
    suspend fun getAll(): List<LugarEntity>

    @Query("SELECT * FROM lugares WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): LugarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(lugares: List<LugarEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lugar: LugarEntity)

    @Query("DELETE FROM lugares")
    suspend fun clearAll()
}
