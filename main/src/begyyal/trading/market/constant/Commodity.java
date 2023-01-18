package begyyal.trading.market.constant;

public enum Commodity implements Product {
    Silver,
    Gold,
    Platinum,
    CrudeOil;

    private Commodity() {
    }
    
    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Commodity;
    }
}
