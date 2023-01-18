package begyyal.trading.market.object;

import begyyal.trading.market.constant.ProductCategory;

public class CommodityDataSet implements MarketData {
    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Commodity;
    }
}
