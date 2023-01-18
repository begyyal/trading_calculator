package begyyal.trading.gui.object;

import begyyal.trading.market.constant.Product;
import begyyal.trading.market.object.ProductKey;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ProductState<T extends Product> {

    public final ProductKey<T> key;
    public final DoubleProperty quote = new SimpleDoubleProperty();
    public final DoubleProperty ceil = new SimpleDoubleProperty();
    public final DoubleProperty floor = new SimpleDoubleProperty();
    public final DoubleProperty netPos = new SimpleDoubleProperty();
    public final DoubleProperty ifCeil = new SimpleDoubleProperty();
    public final DoubleProperty ifFloor = new SimpleDoubleProperty();

    public ProductState(T type) {
	this.key = new ProductKey<>(type);
    }

    public ProductState(T type, boolean isFuture) {
	this.key = new ProductKey<>(type, isFuture);
    }
}
