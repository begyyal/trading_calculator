package begyyal.trading.market.object;

import java.util.Objects;

import begyyal.trading.market.constant.Product;

public class ProductKey<T extends Product> {
    public final T type;
    public final boolean isFuture;

    public ProductKey(T type, boolean isFuture) {
	this.type = type;
	this.isFuture = isFuture;
    }

    public ProductKey(T type) {
	this.type = type;
	this.isFuture = false;
    }

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof ProductKey))
	    return false;
	var casted = (ProductKey<?>) o;
	return casted.type == this.type && (casted.isFuture == this.isFuture);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.type, this.isFuture);
    }
}
