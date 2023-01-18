package begyyal.trading.gui.object;

import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.db.object.TCTable;
import begyyal.trading.market.constant.Product;
import begyyal.trading.market.constant.ProductCategory;
import begyyal.trading.object.ProductState;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class DisplayDataBundle {

    public final XMap<ProductCategory, XList<ProductState<Product>>> productStates;
    public final boolean confirmOverride;
    public final LongProperty tic = new SimpleLongProperty(0);
    public final LongProperty tif = new SimpleLongProperty(0);

    private DisplayDataBundle(boolean confirmOverride) {
	this.productStates = XMapGen.newi();
	for (var pc : ProductCategory.values())
	    this.productStates.put(pc, XListGen.newi());
	this.confirmOverride = confirmOverride;
    }

    public static DisplayDataBundle of(TCTable table) {
	var bundle = new DisplayDataBundle(table.initialized);
	return bundle;
    }

    @SuppressWarnings("unchecked")
    public <T extends Product> ProductState<T> getProductState(T type) {
	return (ProductState<T>) this.productStates.get(type.getCategory())
	    .stream().filter(cs -> cs.type == type)
	    .findFirst().orElse(null);
    }
}