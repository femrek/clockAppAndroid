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
    val ringsList: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}