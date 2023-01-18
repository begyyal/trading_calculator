package begyyal.trading.market.constant;

import begyyal.commons.constant.Ccy;

public enum StonkIndex implements Product {

    EU50(Ccy.Eur),
    HK50(Ccy.Hkd),
    JP225(Ccy.Jpy),
    UK100(Ccy.Gbp),
    US30(Ccy.Usd),
    US100(Ccy.Usd),
    US500(Ccy.Usd);

    public final Ccy ccy;

    private StonkIndex(Ccy ccy) {
	this.ccy = ccy;
    }

    @Override
    public ProductCategory getCategory() {
	return ProductCategory.Stock;
    }
}
