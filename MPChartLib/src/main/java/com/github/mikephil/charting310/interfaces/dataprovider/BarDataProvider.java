/* File changed Copyright 2013 Viatom Technology */

package com.github.mikephil.charting310.interfaces.dataprovider;

import com.github.mikephil.charting310.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();
}
