package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CustomCardEntity::class], version = 1, exportSchema = false)
abstract class DevilsDatabase : RoomDatabase() {
    abstract fun customCardDao(): CustomCardDao

    companion object {
        @Volatile
        private var INSTANCE: DevilsDatabase? = null

        fun getDatabase(context: Context): DevilsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DevilsDatabase::class.java,
                    "devils_deck_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
