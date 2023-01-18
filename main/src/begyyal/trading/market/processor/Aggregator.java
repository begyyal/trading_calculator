package begyyal.trading.market.processor;

import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.object.MarketData;

public interface Aggregator<T extends MarketData> {
    public void fill(T data);

    public T castData(MarketData data);

    public ProductCategory getCategory();
}
