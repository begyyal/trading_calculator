package begyyal.trading.processor;

import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.market.object.MarketDataSet;

public class PLCalculator {

    private PLCalculator() {
    }

    public static void exe(MarketDataSet mktdata, DisplayDataBundle dataBundle) {

	dataBundle.productStates
	    .entrySet().stream()
	    .flatMap(e -> e.getValue().stream())
	    .forEach(state -> {
		var category = state.type.getCategory();
		var data = mktdata.dataset.get(category);
	    });
    }
}
