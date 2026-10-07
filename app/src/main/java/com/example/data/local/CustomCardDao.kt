package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomCardDao {
    @Query("SELECT * FROM custom_cards ORDER BY createdAt DESC")
    fun getAllCustomCards(): Flow<List<CustomCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomCard(card: CustomCardEntity)

    @Delete
    suspend fun deleteCustomCard(card: CustomCardEntity)

    @Query("DELETE FROM custom_cards WHERE id = :id")
    suspend fun deleteById(id: String)
}
