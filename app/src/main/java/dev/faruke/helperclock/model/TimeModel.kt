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
}