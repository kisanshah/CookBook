package com.kisan.cookbook.utils

import java.util.*

class Utils {
    companion object {
        fun getCurrentDate(): String {
            val localCalendar = Calendar.getInstance(TimeZone.getDefault())
            val currentTime = localCalendar.time
            val date = currentTime.toString().substring(4, 10)
            val year = currentTime.toString().substring(30)
            return "$date $year"
        }
    }
}