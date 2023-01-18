package begyyal.trading.db.object;

import java.util.List;
import java.util.stream.Collectors;

import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;

public class TCTable {

    public final XList<TCRecord> records;
    public final boolean initialized;

    private TCTable(XList<TCRecord> records, boolean initialized) {
	this.records = records;
	this.initialized = initialized;
    }

    public static TCTable newi() {
	return new TCTable(XListGen.newi(), true);
    }

    public static TCTable of(List<String> plainLines) {
	var records = plainLines.stream()
	    .map(TCRecord::of)
	    .collect(XListGen.collect());
	return new TCTable(records, false);
    }

    public List<String> serialize() {
	return this.records.stream()
	    .map(TCRecord::serialize)
	    .collect(Collectors.toList());
    }
}
