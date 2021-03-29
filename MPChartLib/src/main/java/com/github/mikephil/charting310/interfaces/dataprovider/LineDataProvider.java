/* File changed Copyright 2013 Viatom Technology */

package com.github.mikephil.charting310.interfaces.dataprovider;

import com.github.mikephil.charting310.components.YAxis;
import com.github.mikephil.charting310.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
