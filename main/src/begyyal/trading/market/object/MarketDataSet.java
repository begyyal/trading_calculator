package begyyal.trading.market.object;

// represents a snapshot
public class MarketDataSet {
    public final StockDataSet stock;
    public final BondDataSet bond;
    public final FxDataSet fx;
    public final CommodityDataSet commodity;

    public MarketDataSet() {
	this.stock = new StockDataSet();
	this.bond = new BondDataSet();
	this.fx = new FxDataSet();
	this.commodity = new CommodityDataSet();
    }
}
