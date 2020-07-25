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

    @Query("UPDATE patternmodel SET title=:title, startHour=:startHour, startMinute=:startMinute, endHour=:endHour, endMinute=:endMinute, ringsList=:ringsList WHERE id = :id")
    suspend fun replacePatternAt(id: Int, title: String, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, ringsList: String)

}