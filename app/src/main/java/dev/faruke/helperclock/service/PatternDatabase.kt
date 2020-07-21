package dev.faruke.helperclock.service

import android.content.Context
import android.security.identity.PersonalizationData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.faruke.helperclock.model.PatternModel

@Database(entities = arrayOf(PatternModel::class), version = 1)
abstract class PatternDatabase : RoomDatabase() {
    abstract fun patternDao() : PatternsDao

    companion object {
        @Volatile private var instance : PatternDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, PatternDatabase::class.java, "patterndatabase"
        ).build()
    }
}