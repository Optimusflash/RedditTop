package com.optimus.reddittop.extensions

import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Dmitriy Chebotar on 18.09.2020.
 */


fun Double.toRelativeDateFormat(): String {
    val currentTime = Calendar.getInstance(Locale.getDefault()).timeInMillis
    val redditTime = this.toLong()*1000

    return when(val timeUnitDiff = currentTime - redditTime){
        in (0 * TimeUnits.SECOND.value)..(1 * TimeUnits.SECOND.value) -> "just now"
        in (1 * TimeUnits.SECOND.value)..(45 * TimeUnits.SECOND.value )-> "a few seconds ago"
        in (45 * TimeUnits.SECOND.value)..(75 * TimeUnits.SECOND.value )-> "1 minute ago"
        in (75 * TimeUnits.SECOND.value)..(45 * TimeUnits.MINUTE.value )-> "${TimeUnits.MINUTE.convertValue(timeUnitDiff)} minutes ago"
        in (45 * TimeUnits.MINUTE.value)..(75 * TimeUnits.MINUTE.value )-> "1 hour ago"
        in (75 * TimeUnits.MINUTE.value)..(22 * TimeUnits.HOUR.value )-> "${TimeUnits.HOUR.convertValue(timeUnitDiff)} hours ago"
        in (22 * TimeUnits.HOUR.value)..(26 * TimeUnits.HOUR.value )-> "1 day ago"
        in (26 * TimeUnits.HOUR.value)..(360 * TimeUnits.DAY.value )-> "${TimeUnits.DAY.convertValue(timeUnitDiff)} days ago"
        else -> "более года назад"
    }
}

enum class TimeUnits(val value: Long) {
    SECOND(1000L),
    MINUTE(SECOND.value * 60),
    HOUR(MINUTE.value * 60),
    DAY(HOUR.value * 24);

    fun convertValue(timeUnitDiff: Long): String{
        val result = (timeUnitDiff.toDouble() / this.value).roundToInt()
        return result.toString()
    }

//    private fun plural(value: Int): String {
//        val plurals = mapOf(
//            SECOND to Triple("секунду", "секунды", "секунд"),
//            MINUTE to Triple("минуту", "минуты", "минут"),
//            HOUR to Triple("час", "часа", "часов"),
//            DAY to Triple("день", "дня", "дней")
//        )
//
//        val mark10 = value % 10
//        val mark100 = value % 100
//
//        return when {
//            mark100 in 10..20 -> "$value ${plurals[this]?.third}"
//            mark10 == 1 -> "$value ${plurals[this]?.first}"
//            mark10 in 2..4 -> "$value ${plurals[this]?.second}" //-
//            else -> "$value ${plurals[this]?.third}"
//        }
//    }
}