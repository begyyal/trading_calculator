package begyyal.trading.object;

import begyyal.commons.constant.Stonk;
import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.constant.CcyPair;
import begyyal.trading.constant.Commodity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DisplayDataBundle {

    private final XMap<Class<?>, ObservableList<CategoryState<?>>> categoryStates;

    public DisplayDataBundle() {
	this.categoryStates = XMapGen.newi();
	this.categoryStates
	    .append(Stonk.class, FXCollections.observableArrayList())
	    .append(CcyPair.class, FXCollections.observableArrayList())
	    .append(Commodity.class, FXCollections.observableArrayList());
    }

    public ObservableList<CategoryState<?>> getCategoryStates(Class<?> clazz) {
	return this.categoryStates.get(clazz);
    }
}
