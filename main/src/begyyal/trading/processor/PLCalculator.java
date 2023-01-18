package begyyal.trading.processor;

import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.market.object.MarketDataSet;

public class PLCalculator {

    private PLCalculator() {
    }

    public static void exe(MarketDataSet mktdata, DisplayDataBundle dataBundle) {

	// fill quote
	dataBundle.productStates
	    .entrySet().stream()
	    .flatMap(e -> e.getValue().stream())
	    .forEach(state -> {
		var priceSet = mktdata.getPriceSet(state.key);
		if (priceSet != null)
		    state.quote.set(priceSet.current);
	    });
    }
}
