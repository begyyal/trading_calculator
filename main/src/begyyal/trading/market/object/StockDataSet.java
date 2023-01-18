package begyyal.trading.market.object;

import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.constant.StonkIndex;

public class StockDataSet implements MarketData {

    public final XMap<ProductKey<StonkIndex>, PriceSet> priceMap;

    public StockDataSet() {
	this.priceMap = XMapGen.newi();
    }

    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Stock;
    }

    @Override
    public PriceSet getPriceSet(ProductKey<?> key) {
	return this.priceMap.get(key);
    }
}
