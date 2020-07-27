package dev.faruke.helperclock.util

import android.content.Context
import android.content.SharedPreferences

class LastUsedPattern {

    companion object{

        var sharedPreferences: SharedPreferences? = null

        private fun init(context: Context) {
            if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences("dev.faruke.helperclock", Context.MODE_PRIVATE)
        }

        fun saveLastPattern(context: Context, id: Int) {
            init(context)
            sharedPreferences!!.edit().putInt("patternId", id).apply()
        }

        fun getLastPattern(context: Context) : Int {
            init(context)
            return sharedPreferences!!.getInt("patternId", -1)
        }

    }

}