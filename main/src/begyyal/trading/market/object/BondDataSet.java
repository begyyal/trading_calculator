package begyyal.trading.market.object;

import begyyal.trading.market.constant.ProductCategory;

public class BondDataSet implements MarketData {
    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Bond;
    }
}
