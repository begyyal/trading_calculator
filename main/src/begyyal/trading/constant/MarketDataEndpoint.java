package begyyal.trading.constant;

import java.net.URI;

public enum MarketDataEndpoint {
    RESULTS("https://app.splatoon2.nintendo.net/api/results");

    public final String url;

    private MarketDataEndpoint(String url) {
	this.url = url;
    }

    public URI toURI() {
	return URI.create(this.url);
    }
}
