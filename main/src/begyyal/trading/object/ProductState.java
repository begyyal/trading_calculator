package begyyal.trading.object;

import begyyal.trading.market.constant.Product;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ProductState<T extends Product> {

    public final T type;
    // TODO
    public final boolean isFuture = false;
    public final DoubleProperty quote = new SimpleDoubleProperty();
    public final DoubleProperty ceil = new SimpleDoubleProperty();
    public final DoubleProperty floor = new SimpleDoubleProperty();
    public final DoubleProperty netPos = new SimpleDoubleProperty();
    public final DoubleProperty ifCeil = new SimpleDoubleProperty();
    public final DoubleProperty ifFloor = new SimpleDoubleProperty();

    public ProductState(T type) {
	this.type = type;
    }
}
