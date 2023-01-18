package begyyal.trading.market.processor;

import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.object.CommodityDataSet;
import begyyal.trading.market.object.MarketData;

public class CommodityPriceAggregator implements Aggregator<CommodityDataSet> {
    public CommodityPriceAggregator() {

    }

    @Override
    public void fill(CommodityDataSet data) {
	// TODO Auto-generated method stub

    }

    @Override
    public ProductCategory getCategory() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CommodityDataSet castData(MarketData data) {
	return CommodityDataSet.class.cast(data);
    }
}
