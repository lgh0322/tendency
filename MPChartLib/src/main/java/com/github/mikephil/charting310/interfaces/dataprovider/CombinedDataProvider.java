/* File changed Copyright 2013 Viatom Technology */

package com.github.mikephil.charting310.interfaces.dataprovider;

import com.github.mikephil.charting310.data.CombinedData;

/**
 * Created by philipp on 11/06/16.
 */
public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
