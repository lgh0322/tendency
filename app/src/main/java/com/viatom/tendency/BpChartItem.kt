package com.viatom.tendency

import java.util.*

data class BpChartItem(val sys : Int, val dis : Int, val time: Long) {
    fun getDate() : Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        val zoneOffset = calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000)
        calendar.add(Calendar.HOUR_OF_DAY, -zoneOffset)
        return calendar.time
    }
//    fun getDate() : Date = Date(time)

    override fun toString(): String {
        return "BpChartItem(sys=$sys, dis=$dis, time=$time)"
    }
}