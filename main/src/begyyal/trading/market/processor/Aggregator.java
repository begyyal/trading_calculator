package begyyal.trading.market.processor;

import begyyal.trading.market.object.MarketDataSet;

public interface Aggregator {
    public void fill(MarketDataSet dataSet);
}
