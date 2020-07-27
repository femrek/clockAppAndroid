package dev.faruke.helperclock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PatternModel(

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,


    @ColumnInfo(name = "startHour")
    @SerializedName("startHour")
    val startHour: Int,


    @ColumnInfo(name = "startMinute")
    @SerializedName("startHour")
    val startMinute: Int,


    @ColumnInfo(name = "endHour")
    @SerializedName("startHour")
    val endHour: Int,


    @ColumnInfo(name = "endMinute")
    @SerializedName("startHour")
    val endMinute: Int,


    @ColumnInfo(name = "ringsList")
    @SerializedName("startHour")
    val ringsList: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    fun isEqual(otherPattern: PatternModel): Boolean {
        if (otherPattern.id != id) return false
        if (
            otherPattern.title == title &&
            otherPattern.startHour == startHour &&
            otherPattern.startMinute == startMinute &&
            otherPattern.endHour == endHour &&
            otherPattern.endMinute == endMinute &&
            otherPattern.ringsList == ringsList
        ) return true
        return false
    }

    override fun toString(): String {
        return "id: $id | title: $title | startHour: $startHour | startMinute: $startMinute | endHour: $endHour | endMinute: $endMinute | ringsList: $ringsList"
    }
}