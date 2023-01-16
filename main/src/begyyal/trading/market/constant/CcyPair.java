package begyyal.trading.market.constant;

import begyyal.commons.constant.Ccy;

public enum CcyPair {
    AUD_CAD(Ccy.Aud, Ccy.Cad),
    AUD_JPY(Ccy.Aud, Ccy.Jpy),
    CAD_CHF(Ccy.Cad, Ccy.Chf),
    CAD_JPY(Ccy.Cad, Ccy.Jpy),
    EUR_AUD(Ccy.Eur, Ccy.Aud),
    EUR_CAD(Ccy.Eur, Ccy.Cad),
    EUR_CHF(Ccy.Eur, Ccy.Chf),
    EUR_GBP(Ccy.Eur, Ccy.Gbp),
    EUR_JPY(Ccy.Eur, Ccy.Jpy),
    EUR_TRY(Ccy.Eur, Ccy.Try),
    EUR_USD(Ccy.Eur, Ccy.Usd),
    EUR_ZAR(Ccy.Eur, Ccy.Zar),
    GBP_CHF(Ccy.Gbp, Ccy.Chf),
    GBP_JPY(Ccy.Gbp, Ccy.Jpy),
    GBP_USD(Ccy.Gbp, Ccy.Usd),
    USD_CHF(Ccy.Usd, Ccy.Chf),
    USD_JPY(Ccy.Usd, Ccy.Jpy),
    USD_MXN(Ccy.Usd, Ccy.Mxn),
    USD_TRY(Ccy.Usd, Ccy.Try),
    USD_ZAR(Ccy.Usd, Ccy.Zar);
    
    public final Ccy left;
    public final Ccy right;

    private CcyPair(Ccy left, Ccy right) {
	this.left = left;
	this.right = right;
    }
}
