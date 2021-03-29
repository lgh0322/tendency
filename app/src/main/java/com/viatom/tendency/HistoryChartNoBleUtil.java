package com.viatom.tendency;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;


import com.github.mikephil.charting310.charts.CombinedChart;
import com.github.mikephil.charting310.charts.ScatterChart;
import com.github.mikephil.charting310.components.AxisBase;
import com.github.mikephil.charting310.components.Legend;
import com.github.mikephil.charting310.components.XAxis;
import com.github.mikephil.charting310.components.YAxis;
import com.github.mikephil.charting310.data.CombinedData;
import com.github.mikephil.charting310.data.Entry;
import com.github.mikephil.charting310.data.LineData;
import com.github.mikephil.charting310.data.LineDataSet;
import com.github.mikephil.charting310.data.ScatterData;
import com.github.mikephil.charting310.data.ScatterDataSet;
import com.github.mikephil.charting310.formatter.ValueFormatter;
import com.github.mikephil.charting310.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting310.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting310.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HistoryChartNoBleUtil {
    public static void initChart(Context context, CombinedChart mChart) {
        if (context == null) {
            return;
        }
        // no description text
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(context.getResources().getColor(R.color.bpmColorPrimary));
        mChart.setBorderColor(Color.WHITE);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setNoDataText(context.getString(R.string.bp_no_record));
        mChart.setNoDataTextColor(Color.WHITE);
        mChart.setDrawBorders(false);
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        // x axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(context.getResources().getColor(R.color.bpm_lightGray));
        xAxis.setSpaceMin(4f);
        xAxis.setDrawGridLines(false);//时间轴无线
        xAxis.setGranularity(1);
        xAxis.setLabelCount(7);

        Locale locale = Locale.getDefault();
        SimpleDateFormat mFormat;
        if (Locale.CHINESE.getLanguage().equals(locale.getLanguage())) {
            mFormat = new SimpleDateFormat("MM.d", Locale.CHINESE);
        } else {
            mFormat = new SimpleDateFormat("MMM d", Locale.US);
        }

        xAxis.setValueFormatter(new ValueFormatter() {
            // "MMM dd"
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });


        // Y axis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(context.getResources().getColor(R.color.bpm_lightGray));
        leftAxis.setGridDashedLine(new DashPathEffect(new float[]{5, 20, 5, 20}, 10));
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setTextSize(10f);
        leftAxis.setAxisLineColor(context.getResources().getColor(R.color.bpm_lightGray));
        leftAxis.setTextColor(context.getResources().getColor(R.color.bpm_lightGray));
        leftAxis.setLabelCount(5);
        leftAxis.setAxisMinimum(50f);
        leftAxis.setAxisMaximum(200f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawAxisLine(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setDrawGridLines(false);
        rightAxis.setGridColor(context.getResources().getColor(R.color.bpm_lightGray));
        rightAxis.setTextSize(10f);
        rightAxis.setAxisLineColor(context.getResources().getColor(R.color.bpm_lightGray));
        rightAxis.setTextColor(Color.TRANSPARENT);
        rightAxis.setLabelCount(5);
        rightAxis.setAxisMinimum(50f);
        rightAxis.setAxisMaximum(200f);

        mChart.invalidate();
    }

    public static void refreshChart(Context context, CombinedChart mChart, List<BpChartItem> results) {
        if (context == null) {
            return;
        }
        if (results == null || results.size() == 0) {
            results=new ArrayList<>();
            results.add(new BpChartItem(-100,-100, System.currentTimeMillis()));
        }


        // set chart data
        ArrayList<Entry> sysValues = new ArrayList<>();
        ArrayList<Entry> diaValues = new ArrayList<>();

        //按日期分数组
        List<BpSampleItem> bpSampleItems = new ArrayList<>();
        BpSampleItem sampleItem = new BpSampleItem();
//        Date thisDay = makeDateChange(makeZoneOffsetChange(new Date(results.get(0).getTime())), 0);
        Date thisDay = makeDateChange(new Date(results.get(0).getTime()), 0);
        Log.d("BpChart", "refreshChart: thisDay == " + Utilsv.Companion.getDateStr(thisDay, "yyyy/MM/dd HH:mm:ss", Locale.US));
        sampleItem.setDate(thisDay);
        for (BpChartItem realmResult : results) {
//            Date itemDate = makeZoneOffsetChange(new Date(realmResult.getTime()));
            Date itemDate = new Date(realmResult.getTime());
            if (isSameDay(thisDay, itemDate)) {
                sampleItem.addSys(realmResult.getSys());
                sampleItem.addDis(realmResult.getDis());
            } else {
                bpSampleItems.add(sampleItem);

                thisDay = makeDateChange(itemDate, 0);
                sampleItem = new BpSampleItem();
                sampleItem.setDate(thisDay);
                sampleItem.addSys(realmResult.getSys());
                sampleItem.addDis(realmResult.getDis());
            }
        }
        bpSampleItems.add(sampleItem);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        float totalMaxSys = 194;
        float totalMinDia = 56;
        for (BpSampleItem item : bpSampleItems) {
            Log.d("BpChart", "refreshChart: itemDate == " + Utilsv.Companion.getDateStr(item.getDate().getTime(), "yyyy/MM/dd HH:mm:ss", Locale.US));
            long x = TimeUnit.MILLISECONDS.toDays(makeDateChange(item.getDate(), 1).getTime());
            Entry sysMin = new Entry(x, item.getMinSys());
            Entry sysMax = new Entry(x, item.getMaxSys());
            Entry diaMin = new Entry(x, item.getMinDia());
            Entry diaMax = new Entry(x, item.getMaxDia());

            if (item.getMaxSys() > totalMaxSys && item.getMaxSys() != -50.0f) {
                totalMaxSys = item.getMaxSys();
            }

            if (item.getMinDia() < totalMinDia && item.getMinDia() != -50.0f) {
                totalMinDia = item.getMinDia();
            }

            sysValues.add(sysMin);
            sysValues.add(sysMax);
            diaValues.add(diaMin);
            diaValues.add(diaMax);

            //line entries
            ArrayList<Entry> sysLineValues = new ArrayList<>();
            sysLineValues.add(sysMin);
            sysLineValues.add(sysMax);
            ArrayList<Entry> diaLineValues = new ArrayList<>();
            diaLineValues.add(diaMin);
            diaLineValues.add(diaMax);

            LineDataSet sysLineSet = createLineSet(context, sysLineValues, R.color.bpm_sys_mark);
            LineDataSet diaLineSet = createLineSet(context, diaLineValues, R.color.bpm_dia_mark);
            lineDataSets.add(sysLineSet);
            lineDataSets.add(diaLineSet);
        }

        CombinedData combinedData = new CombinedData();
        combinedData.setData(new LineData(lineDataSets));

        ScatterDataSet sysSet = new ScatterDataSet(sysValues, "SYS");
        ScatterDataSet diaSet = new ScatterDataSet(diaValues, "DIA");

        sysSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        sysSet.setScatterShapeSize(20f);
        sysSet.setColor(context.getResources().getColor(R.color.bpm_sys_mark));
        sysSet.setHighlightEnabled(false);

        diaSet.setScatterShape(ScatterChart.ScatterShape.X);
        diaSet.setShapeRenderer(new MySquareShapeRenderer());
        diaSet.setScatterShapeSize(20f);
        diaSet.setColor(context.getResources().getColor(R.color.bpm_dia_mark));
        diaSet.setHighlightEnabled(false);
        diaSet.setDrawIcons(true);

        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(sysSet);
        dataSets.add(diaSet);

        ScatterData data = new ScatterData(dataSets);
        data.setDrawValues(false);

        long xMax = results.get(results.size() - 1).getTime() / (1000 * 3600 * 24);
        long xMin = results.get(0).getTime() / (1000 * 3600 * 24);
        if (xMax - xMin < 7) {
            xMin = xMax - 7;
        }

        Log.d("BpChart", "refreshChart: xMin itemDate == " + Utilsv.Companion.getDateStr(results.get(results.size() - 1).getTime(), "yyyy/MM/dd HH:mm:ss", Locale.US));
        Log.d("BpChart", "refreshChart: xMax itemDate == " + Utilsv.Companion.getDateStr(results.get(0).getTime(), "yyyy/MM/dd HH:mm:ss", Locale.US));

        float scale = (xMax + 1 - xMin) / 7f;
        mChart.setScaleMinima(scale, 1.0f);
        if (scale > 1) {
            mChart.getViewPortHandler().setMinMaxScaleX(scale, scale);
        } else {
            mChart.getViewPortHandler().setMinMaxScaleX(scale, 1.0f);
        }

        mChart.getViewPortHandler().setMinMaxScaleY(1.0f, 1.0f);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
//
        float maxVal = 200;
        float minVal = 50;
        int labelCount = 4;
        if (totalMaxSys < 195 && totalMinDia > 55) {
            maxVal = 200;
            minVal = 50;
            labelCount = 3;
        } else {
            if (totalMaxSys > 194 && totalMinDia < 56) {
                maxVal = 250;
                minVal = 0;
                labelCount = 5;
            } else if (totalMaxSys > 194) {
                maxVal = 250;
                minVal = 50;
                labelCount = 4;
            } else {
                maxVal = 200;
                minVal = 0;
                labelCount = 4;
            }
        }
        Log.d("BpChart", "refreshChart: totalMaxSys = " + totalMaxSys + " totalMinDia == " + totalMinDia);
        Log.d("BpChart", "refreshChart: maxVal = " + maxVal + " minVal == " + minVal);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(minVal);
        leftAxis.setAxisMaximum(maxVal);
        leftAxis.setLabelCount(labelCount);
//		leftAxis.mAxisMaximum = maxVal;
//		leftAxis.mAxisMinimum = minVal;

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMinimum(minVal);
        rightAxis.setAxisMaximum(maxVal);
        rightAxis.setLabelCount(labelCount);
//		rightAxis.mAxisMaximum = maxVal;
//		rightAxis.mAxisMinimum = minVal;

        XAxis axis = mChart.getXAxis();
        axis.setAxisMinimum(xMin - 1);
        axis.setAxisMaximum(xMax + 1.5f);
        mChart.moveViewToX(xMax + 1.5f);
        mChart.resetZoom();


        combinedData.setData(data);
        mChart.setData(combinedData);

        mChart.invalidate();
        if (scale > 1) {
            mChart.getViewPortHandler().setMinMaxScaleX(1.0f, scale);
        } else {
            mChart.getViewPortHandler().setMinMaxScaleX(scale, 1.0f);
        }
    }

    private static LineDataSet createLineSet(Context context, ArrayList<Entry> lineValues, int lineColorResId) {
        LineDataSet set = new LineDataSet(lineValues, "");
        set.setColor(context.getResources().getColor(lineColorResId), 0xFF / 2);
        set.setLineWidth(6f);
        set.setHighlightEnabled(false);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        return set;
    }

    /**
     * 生成一天最后一小时Date，用于产生天列表
     *
     * @param date
     */
    private static Date makeDateChange(Date date, int dayChange) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dayChange);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    private static Date makeZoneOffsetChange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000);
//		Log.d("BpChart", "makeZoneOffsetChange: zoneOffset == " + zoneOffset);
        calendar.add(Calendar.HOUR_OF_DAY, -zoneOffset);

        return calendar.getTime();
    }

    /**
     * 判断两个date是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    private static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        if (date1.getDate() == date2.getDate()
                && date1.getMonth() == date2.getMonth()
                && date1.getYear() == date2.getYear()) {
            return true;
        } else {
            return false;
        }
    }
}
