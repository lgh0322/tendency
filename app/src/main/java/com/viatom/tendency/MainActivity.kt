package com.viatom.tendency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.github.mikephil.charting310.charts.CombinedChart

class MainActivity : AppCompatActivity() {
    lateinit var b: CombinedChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        b=findViewById(R.id.bp_history_chart)
        HistoryChartNoBleUtil.initChart(this, b)
        val dataList: MutableList<BpChartItem> = ArrayList ()
        dataList.add(BpChartItem(125,80,System.currentTimeMillis()))
        HistoryChartNoBleUtil.refreshChart(this, b, dataList)
    }
}