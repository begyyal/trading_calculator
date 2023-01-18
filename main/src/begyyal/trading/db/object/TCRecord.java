package begyyal.trading.db.object;

import java.util.StringTokenizer;

import begyyal.commons.constant.Strs;

public class TCRecord {

    public final int categoryId;
    public final boolean isFuture;
    public final Double quote;
    public final Double ceil;
    public final Double floor;
    public final Double netPos;

    private TCRecord(
	int categoryId,
	boolean isFuture,
	Double quote,
	Double ceil,
	Double floor,
	Double netPos) {
	this.categoryId = categoryId;
	this.isFuture = isFuture;
	this.quote = quote;
	this.ceil = ceil;
	this.floor = floor;
	this.netPos = netPos;
    }

    public static TCRecord of(String pl) {
	var st = new StringTokenizer(pl, Strs.comma);
	return new TCRecord(
	    Integer.parseInt(st.nextToken()),
	    "1".equals(st.nextToken()),
	    parseDouble(st.nextToken()),
	    parseDouble(st.nextToken()),
	    parseDouble(st.nextToken()),
	    parseDouble(st.nextToken()));
    }

    private static Double parseDouble(String token) {
	return "n".equals(token) ? null : Double.parseDouble(token);
    }

    public String serialize() {
	var vArray = new Object[] {
		this.categoryId,
		this.isFuture ? 1 : 0,
		this.quote == null ? "n" : this.quote,
		this.ceil == null ? "n" : this.ceil,
		this.floor == null ? "n" : this.floor,
		this.netPos == null ? "n" : this.netPos };
	return String.join(Strs.comma, (String[]) vArray);
    }
}
