package dev.faruke.helperclock.util

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
    }
}
