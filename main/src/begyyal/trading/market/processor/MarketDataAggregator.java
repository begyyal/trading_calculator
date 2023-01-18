package begyyal.trading.market.processor;

import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.trading.market.object.MarketData;
import begyyal.trading.market.object.MarketDataSet;

public class MarketDataAggregator {
    private final XList<Aggregator<? extends MarketData>> aggregatorList;

    public MarketDataAggregator() {
	this.aggregatorList = XListGen.of(
	    new StockPriceAggregator(),
	    new BondYieldAggregator(),
	    new FxRateAggregator(),
	    new CommodityPriceAggregator());
    }

    public void fill(MarketDataSet dataSet) {
	this.aggregatorList.forEach(ag -> this.aggregate(dataSet, ag));
    }

    public <T extends MarketData> void aggregate(MarketDataSet dataSet, Aggregator<T> ag) {
	var data = dataSet.dataset.get(ag.getCategory());
	ag.fill(ag.castData(data));
    }
}
