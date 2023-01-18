package begyyal.trading.market.processor;

import begyyal.commons.constant.Strs;
import begyyal.commons.object.collection.XMap;
import begyyal.commons.util.function.XNumbers;
import begyyal.trading.logger.TCLogger;
import begyyal.trading.market.constant.MarketDataDomain;
import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.market.constant.StonkIndex;
import begyyal.trading.market.object.MarketData;
import begyyal.trading.market.object.PriceSet;
import begyyal.trading.market.object.ProductKey;
import begyyal.trading.market.object.StockDataSet;
import begyyal.web.WebResourceGetter;
import begyyal.web.html.object.HtmlObject;

public class StockPriceAggregator implements Aggregator<StockDataSet> {

    private static final String spotUrl = //
	    MarketDataDomain.InvestingDotCom.url + "/indices/world-indices";
    private static final XMap<StonkIndex, String> idcIndicesIdMap = XMap.XMapGen.newi();
    private static final XMap<StonkIndex, String> idcFuturePathMap = XMap.XMapGen.newi();
    {
	idcIndicesIdMap
	    .append(StonkIndex.JP225, "178")
	    .append(StonkIndex.US30, "169")
	    .append(StonkIndex.US500, "166");
	idcFuturePathMap
	    .append(StonkIndex.US30,
		MarketDataDomain.InvestingDotCom.url + "/indices/us-30-futures")
	    .append(StonkIndex.US500,
		MarketDataDomain.InvestingDotCom.url + "/indices/us-spx-500-futures");
    }

    public StockPriceAggregator() {
    }

    public void fill(StockDataSet dataSet) {
	var idcIndicesHo = WebResourceGetter.getHtmlObject(spotUrl);
	idcIndicesIdMap.entrySet().forEach(e -> {
	    var current = this.getSpotCurrentPriceFromIdc(idcIndicesHo, e.getValue(), e.getKey());
	    dataSet.priceMap.put(new ProductKey<>(e.getKey(), false), new PriceSet(current));
	});
	idcFuturePathMap.entrySet().forEach(e -> {
	    var current = this.getFutureCurrentPriceFromIdc(e.getValue(), e.getKey());
	    dataSet.priceMap.put(new ProductKey<>(e.getKey(), true), new PriceSet(current));
	});
    }

    private Double getSpotCurrentPriceFromIdc(HtmlObject root, String id, StonkIndex type) {
	var ho = root.getElementById("pair_" + id);
	if (ho == null) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, false);
	    return null;
	}
	var ho2 = ho.getElementsByClass("pid-" + id + "-last");
	if (ho2.isEmpty()) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, false);
	    return null;
	}
	var plainStr = ho2.getTip().getContents().getTip();
	if (plainStr == null) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, false);
	    return null;
	}
	var price = plainStr.replace(Strs.comma, Strs.empty);
	return XNumbers.checkIfParsable(price, true) ? Double.valueOf(price) : null;
    }

    private Double getFutureCurrentPriceFromIdc(String url, StonkIndex type) {
	var root = WebResourceGetter.getHtmlObject(url);
	var ho2 = root.select(ho -> ho.getProperties().entrySet().stream()
	    .filter(prop -> "data-test".equals(prop.getKey())
		    && "instrument-header-details".equals(prop.getValue()))
	    .findAny().isPresent());
	if (ho2.isEmpty()) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, true);
	    return null;
	}
	var ho3 = ho2.get(0).getChildren().get(0)
	    .select(ho -> ho.getProperties().entrySet().stream()
		.filter(prop -> "data-test".equals(prop.getKey())
			&& "instrument-price-last".equals(prop.getValue()))
		.findAny().isPresent());
	if (ho3.isEmpty()) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, true);
	    return null;
	}
	var plainStr = ho3.getTip().getContents().getTip();
	if (plainStr == null) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, true);
	    return null;
	}
	var price = plainStr.replace(Strs.comma, Strs.empty);
	return XNumbers.checkIfParsable(price, true) ? Double.valueOf(price) : null;
    }

    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Stock;
    }

    @Override
    public StockDataSet castData(MarketData data) {
	return StockDataSet.class.cast(data);
    }
}
