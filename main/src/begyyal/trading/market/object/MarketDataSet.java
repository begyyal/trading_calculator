package begyyal.trading.market.object;

import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.market.constant.ProductCategory;

// represents a snapshot
public class MarketDataSet {

    public final XMap<ProductCategory, MarketData> dataset;

    public MarketDataSet() {
	this.dataset = XMapGen.newi();
	this.appendMarketData(new StockDataSet());
	this.appendMarketData(new BondDataSet());
	this.appendMarketData(new FxDataSet());
	this.appendMarketData(new CommodityDataSet());
    }

    private void appendMarketData(MarketData data) {
	this.dataset.append(data.getCategory(), data);
    }
}
