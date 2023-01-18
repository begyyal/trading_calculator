package begyyal.trading.market.constant;

import java.net.URI;

public enum MarketDataDomain {
    InvestingDotCom("https://www.investing.com");

    public final String url;

    private MarketDataDomain(String url) {
	this.url = url;
    }

    public URI toURI() {
	return URI.create(this.url);
    }
}
