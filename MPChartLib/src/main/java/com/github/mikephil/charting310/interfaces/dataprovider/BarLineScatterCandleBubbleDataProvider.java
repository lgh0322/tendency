/* File changed Copyright 2013 Viatom Technology */

package com.github.mikephil.charting310.interfaces.dataprovider;

import com.github.mikephil.charting310.components.YAxis.AxisDependency;
import com.github.mikephil.charting310.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting310.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
