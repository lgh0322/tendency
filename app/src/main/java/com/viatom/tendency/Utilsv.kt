package com.viatom.tendency

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Utilsv {
    companion object {

        fun convertDpToPixel(context: Context, dp: Float) = dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        fun convertPixelsToDp(context: Context, px: Float) = px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

        fun getScreenWidth(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
        fun getScreenHeight(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

        fun getStringWidth(paint: Paint, str: String) = paint.measureText(str)
        fun getStringHeight(paint: Paint): Float {
            val fontMetrics = paint.fontMetrics
            return Math.abs(fontMetrics.top) + Math.abs(fontMetrics.bottom)
        }
        fun getStringTopHeight(paint: Paint): Float {
            val fontMetrics = paint.fontMetrics
            return Math.abs(fontMetrics.top)
        }
        fun getStringBottomHeight(paint: Paint): Float {
            val fontMetrics = paint.fontMetrics
            return Math.abs(fontMetrics.bottom)
        }
        fun getDateList(date: Date, pattern: String, totalDay: Int = 30, size: Int = 7, duration: Int = 5, locale: Locale = Locale.US): ArrayList<String> {
            val list = ArrayList<Date>()
            val format = SimpleDateFormat(pattern, locale)
            val calendar = Calendar.getInstance()
            calendar.time = date
            var i = 0
            while(i < size) {
                if(i != 0) {
                    calendar.add(Calendar.DATE, - duration)
                }
                list.add(calendar.time)
                i++
            }
            val listString = ArrayList<String>()
            listString.addAll(list.map { format.format(it) })
            return listString
        }

        fun getDateListGMT0(date: Date, pattern: String, totalDay: Int = 30, size: Int = 7, duration: Int = 5, locale: Locale = Locale.US): ArrayList<String> {
            val list = ArrayList<Date>()
            val format = SimpleDateFormat(pattern, locale)
            format.timeZone = TimeZone.getTimeZone("GMT+0")
            val calendar = Calendar.getInstance()
            calendar.time = date
            var i = 0
            while(i < size) {
                if(i != 0) {
                    calendar.add(Calendar.DATE, - duration)
                }
                list.add(calendar.time)
                i++
            }
            val listString = ArrayList<String>()
            listString.addAll(list.map { format.format(it) })
            return listString
        }

        fun getDateStr(date: Date, pattern: String, locale: Locale = Locale.US): String{
            val format = SimpleDateFormat(pattern, locale)
//            format.timeZone = TimeZone.getTimeZone("GMT+0")
            return format.format(date)
        }
        fun getDateStr(time: Long, pattern: String, locale: Locale = Locale.US) : String{
            val format = SimpleDateFormat(pattern, locale)
//            format.timeZone = TimeZone.getTimeZone("GMT+0")
            return format.format(Date(time))
        }

        fun getDateStrGMT0(date: Date, pattern: String, locale: Locale = Locale.US): String{
            val format = SimpleDateFormat(pattern, locale)
            format.timeZone = TimeZone.getTimeZone("GMT+0")
            return format.format(date)
        }
        fun getDateStrGMT0(time: Long, pattern: String, locale: Locale = Locale.US) : String{
            val format = SimpleDateFormat(pattern, locale)
            format.timeZone = TimeZone.getTimeZone("GMT+0")
            return format.format(Date(time))
        }

        fun makeZoneOffsetChange(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val zoneOffset = calendar[Calendar.ZONE_OFFSET] / (60 * 60 * 1000)
            calendar.add(Calendar.HOUR_OF_DAY, -zoneOffset)
            return calendar.time
        }

        fun getVersionName(context: Context): String {
            var version = ""
            try { // 获取packagemanager的实例
                val packageManager = context.packageManager
                // getPackageName()获取当前类的包名
                val packInfo = packageManager.getPackageInfo(context.packageName, 0)
                version = packInfo.versionName
            } catch (e: Exception) {
            }
            return version
        }
    }
}