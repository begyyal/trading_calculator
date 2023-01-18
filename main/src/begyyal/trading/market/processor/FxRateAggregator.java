package begyyal.trading.market.processor;

import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.object.FxDataSet;
import begyyal.trading.market.object.MarketData;

public class FxRateAggregator implements Aggregator<FxDataSet> {
    public FxRateAggregator() {

    }

    @Override
    public void fill(FxDataSet data) {
	// TODO Auto-generated method stub

    }

    @Override
    public ProductCategory getCategory() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public FxDataSet castData(MarketData data) {
	return FxDataSet.class.cast(data);
    }
}
