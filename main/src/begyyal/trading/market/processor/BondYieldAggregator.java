package begyyal.trading.market.processor;

import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.object.BondDataSet;
import begyyal.trading.market.object.MarketData;

public class BondYieldAggregator implements Aggregator<BondDataSet> {
    public BondYieldAggregator() {

    }

    @Override
    public void fill(BondDataSet data) {
	// TODO Auto-generated method stub

    }

    @Override
    public ProductCategory getCategory() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public BondDataSet castData(MarketData data) {
	return BondDataSet.class.cast(data);
    }
}
