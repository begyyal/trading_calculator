package begyyal.trading.market.constant;

public enum Bond implements Product {
    ;

    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Bond;
    }
}
