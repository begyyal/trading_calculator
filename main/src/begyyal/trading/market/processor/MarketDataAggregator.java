package begyyal.trading.market.processor;

import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.trading.market.object.MarketDataSet;

public class MarketDataAggregator {
    private final XList<Aggregator> aggregatorList;

    public MarketDataAggregator() {
	this.aggregatorList = XListGen.of(
	    new StockPriceAggregator(),
	    new BondYieldAggregator(),
	    new FxRateAggregator(),
	    new CommodityPriceAggregator());
    }

    public void fill(MarketDataSet dataSet) {
	this.aggregatorList.forEach(ag -> ag.fill(dataSet));
    }
}
