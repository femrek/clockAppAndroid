package dev.faruke.helperclock.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import dev.faruke.helperclock.model.TimeModel

abstract class UtilFuns {

    companion object {
        fun nextSecond(currentTime: TimeModel): TimeModel {
            val currentSec = currentTime.second
            val currentMin = currentTime.minute
            val currentHour = currentTime.hour

            return if (currentSec < 59) {
                TimeModel(currentHour, currentMin, currentSec + 1)
            } else {
                if (currentMin < 59) {
                    TimeModel(currentHour, currentMin + 1, 0)
                } else {
                    if (currentHour < 23) {
                        TimeModel(currentHour + 1, 0, 0)
                    } else {
                        TimeModel(0, 0, 0)
                    }
                }
            }
        }

        fun dpToPx(context: Context, dp: Float) : Float {
            val r: Resources = context.resources
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.displayMetrics
            )
        }

        fun spToPx(context: Context, sp: Float) : Float {
            val r: Resources = context.resources
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                r.displayMetrics
            )
        }

        fun clockToString(hour: Int, minute: Int, second: Int?) : String {
            var result = ""

            if (hour < 10) result +="0"
            result += "$hour:"

            if (minute < 10) result += "0"
            result += "$minute"

            if (second != null) {
                result += if (second < 10) ":0$second" else ":$second"
            }

            return result
        }

        fun convertRingsListToString(list: ArrayList<ArrayList<Int>>): String {
            var result = ""

            for (row in list) {
                for ((index, cell) in row.withIndex()) {
                    result += "$cell"
                    if (index < row.size - 1) result += ","
                }
                result += ";"
            }

            return result
        }

        fun convertRingsStringToArrayList(string: String): ArrayList<ArrayList<Int>> {
            val result: ArrayList<ArrayList<Int>> = ArrayList()

            var startIndex = 0
            val rowCount = string.count { ";".contains(it) }
            for (i in 0 until rowCount) {
                val rowList: ArrayList<Int> = ArrayList()
                val rowString = string.substring(startIndex, string.indexOf(';', startIndex))
                var commaStartIndex = 0
                for (j in 0..rowString.count { ",".contains(it) }) {
                    val commaIndex = rowString.indexOf(",", commaStartIndex)
                    rowList.add(
                        rowString.substring(commaStartIndex, (if (commaIndex > 0) commaIndex else rowString.length)).toInt()
                    )
                    commaStartIndex = commaIndex + 1
                }
                startIndex = string.indexOf(";", startIndex) + 1
                result.add(rowList)
            }

            return result
        }

        fun convertRingsStringToTimeModelArrayList(string: String): ArrayList<TimeModel> {
            val result: ArrayList<TimeModel> = ArrayList()

            var startIndex = 0
            val rowCount = string.count { ";".contains(it) }
            for (i in 0 until rowCount) {
                val rowList: ArrayList<Int> = ArrayList()
                val rowString = string.substring(startIndex, string.indexOf(';', startIndex))
                var commaStartIndex = 0
                for (j in 0..rowString.count { ",".contains(it) }) {
                    val commaIndex = rowString.indexOf(",", commaStartIndex)
                    rowList.add(
                        rowString.substring(commaStartIndex, (if (commaIndex > 0) commaIndex else rowString.length)).toInt()
                    )
                    commaStartIndex = commaIndex + 1
                }
                startIndex = string.indexOf(";", startIndex) + 1
                result.add(TimeModel(rowList[0], rowList[1], 0))

            }

            return result
        }
    }
}
