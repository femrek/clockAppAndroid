package dev.faruke.helperclock.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.faruke.helperclock.model.PatternModel

@Dao
interface PatternsDao {

    @Insert
    suspend fun insertPattern(pattern: PatternModel) : Long

    @Query("SELECT * FROM patternmodel WHERE id = :id")
    suspend fun getPattern(id: Int) : PatternModel

    @Query("SELECT * FROM patternmodel")
    suspend fun getAllPatterns() : List<PatternModel>

    @Query("DELETE FROM patternmodel WHERE id = :id")
    suspend fun deletePatternAt(id: Int)

}