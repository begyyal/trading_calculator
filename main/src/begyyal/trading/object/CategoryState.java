package begyyal.trading.object;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CategoryState<T> {

    public final T category;
    public BigDecimal quote = newBigDecimal();
    public BigDecimal ceil = newBigDecimal();
    public BigDecimal floor = newBigDecimal();
    public BigDecimal netPos = newBigDecimal();
    public BigDecimal ifCeil = newBigDecimal();
    public BigDecimal ifFloor = newBigDecimal();

    private CategoryState(T category) {
	this.category = category;
    }

    public static <T> CategoryState<T> newi(T category) {
	return new CategoryState<T>(category);
    }

    private static BigDecimal newBigDecimal() {
	var bd = new BigDecimal(0);
	bd.setScale(4, RoundingMode.DOWN);
	return bd;
    }
}
