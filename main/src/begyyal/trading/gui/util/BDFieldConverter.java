package begyyal.trading.gui.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import begyyal.commons.util.function.XNumbers;
import javafx.util.StringConverter;

public class BDFieldConverter extends StringConverter<BigDecimal> {

    @Override
    public String toString(BigDecimal object) {
	return object == null ? null : object.toString();
    }

    @Override
    public BigDecimal fromString(String str) {
	if (str == null || !XNumbers.checkIfParsable(str, true))
	    return null;
	var bd = new BigDecimal(str);
	bd.setScale(4, RoundingMode.FLOOR);
	return bd;
    }
}
