package begyyal.trading.logger;

import begyyal.trading.market.constant.MarketDataDomain;

public class TCLogger {
    private TCLogger() {
    }

    public static void printScrapingFailure(Enum<?> type1, MarketDataDomain type2, boolean isFuture) {
	System.out.println("[Error] Scraping failed at...");
	System.out.println(" -> " + (isFuture ? "future" : "spot"));
	System.out.println(" -> " + type1);
	System.out.println(" -> " + type2.url);
    }
}
