package begyyal.trading.market.processor;

import begyyal.commons.constant.Stonk;
import begyyal.commons.object.collection.XMap;
import begyyal.commons.util.function.XIntegers;
import begyyal.trading.logger.TCLogger;
import begyyal.trading.market.constant.MarketDataDomain;
import begyyal.trading.market.object.MarketDataSet;
import begyyal.web.WebResourceGetter;
import begyyal.web.html.object.HtmlObject;

public class StockPriceAggregator implements Aggregator {

    private static final String spotUrl = //
	    MarketDataDomain.InvestingDotCom.url + "/indices/world-indices";
    private static final XMap<Stonk, String> idcIndicesIdMap = XMap.XMapGen.newi();
    private static final XMap<Stonk, String> idcFuturePathMap = XMap.XMapGen.newi();
    {
	idcIndicesIdMap
	    .append(Stonk.JP225, "178")
	    .append(Stonk.US30, "169")
	    .append(Stonk.US500, "166");
	idcFuturePathMap
	    .append(Stonk.US30, MarketDataDomain.InvestingDotCom.url + "/indices/us-30-futures")
	    .append(Stonk.US500, MarketDataDomain.InvestingDotCom.url + "/indices/us-spx-500-futures");
    }

    public StockPriceAggregator() {
    }

    public void fill(MarketDataSet dataSet) {
	var idcIndicesHo = WebResourceGetter.getHtmlObject(spotUrl);
	idcIndicesIdMap.entrySet().forEach(e -> {
	    dataSet.stock.spot.put(e.getKey(),
		this.getSpotCurrentPriceFromIdc(idcIndicesHo, e.getValue(), e.getKey()));
	});
	idcFuturePathMap.entrySet().forEach(e -> {
	    dataSet.stock.future.put(e.getKey(),
		this.getFutureCurrentPriceFromIdc(e.getValue(), e.getKey()));
	});
    }

    private Double getSpotCurrentPriceFromIdc(HtmlObject root, String id, Stonk type) {
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
	var price = ho2.getTip().getContents().getTip();
	return XIntegers.checkIfParsable(price) ? Double.valueOf(price) : null;
    }

    private Double getFutureCurrentPriceFromIdc(String url, Stonk type) {
	var root = WebResourceGetter.getHtmlObject(url);
	var ho2 = root.select(ho -> ho.getProperties().entrySet().stream()
	    .filter(prop -> "data-test".equals(prop.getKey())
		    && "instrument-header-details".equals(prop.getValue()))
	    .findAny().isPresent());
	if (ho2.isEmpty()) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, true);
	    return null;
	}
	var ho3 = ho2.getTip().getChildren().getTip()
	    .select(ho -> ho.getProperties().entrySet().stream()
		.filter(prop -> "data-test".equals(prop.getKey())
			&& "instrument-price-last".equals(prop.getValue()))
		.findAny().isPresent());
	if (ho3.isEmpty()) {
	    TCLogger.printScrapingFailure(type, MarketDataDomain.InvestingDotCom, true);
	    return null;
	}
	var price = ho3.getTip().getContents().getTip();
	return XIntegers.checkIfParsable(price) ? Double.valueOf(price) : null;
    }
}