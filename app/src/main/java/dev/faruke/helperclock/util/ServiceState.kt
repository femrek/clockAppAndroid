package dev.faruke.helperclock.util

abstract class ServiceState {

    companion object {
        const val NOT_RUNNING = 0

        const val RESUMED = 1
        const val PAUSED = 2
    }


}