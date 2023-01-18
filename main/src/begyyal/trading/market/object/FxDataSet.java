package begyyal.trading.market.object;

import begyyal.trading.market.constant.ProductCategory;

public class FxDataSet implements MarketData {
    
    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Fx;
    }
}
