package begyyal.trading.market.object;

import begyyal.commons.constant.Stonk;
import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;

public class StockDataSet {

    public final XMap<Stonk, Double> spot;
    public final XMap<Stonk, Double> future;

    public StockDataSet() {
	this.spot = XMapGen.newi();
	this.future = XMapGen.newi();
    }
}
