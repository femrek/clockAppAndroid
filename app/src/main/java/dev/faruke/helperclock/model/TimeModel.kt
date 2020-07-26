package dev.faruke.helperclock.model

import dev.faruke.helperclock.util.UtilFuns.Companion.clockToString

data class TimeModel(
    val hour: Int,
    val minute: Int,
    val second: Int
) {
    override fun toString(): String {
        return clockToString(hour, minute, second)
    }

    fun sumClock(otherClock: TimeModel) : TimeModel {
        var resultHour = hour + otherClock.hour
        var resultMinute = minute + otherClock.minute
        var resultSecond = second + otherClock.second

        if (resultHour > 24) resultHour -= 24
        if (resultMinute > 60) resultMinute -= 60
        if (resultSecond > 60) resultSecond -= 60

        return TimeModel(resultHour, resultMinute, resultSecond)
    }

    fun subtractionClock(otherClock: TimeModel) : TimeModel {
        var resultHour = hour - otherClock.hour
        var resultMinute = minute - otherClock.minute
        var resultSecond = second - otherClock.second

        if (resultHour < 0) resultHour += 24
        if (resultMinute < 0) resultMinute += 60
        if (resultSecond < 0) resultSecond += 60

        return TimeModel(resultHour, resultMinute, resultSecond)
    }
}