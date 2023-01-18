package begyyal.trading.processor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import begyyal.commons.object.collection.PairList;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.constant.FuncConst;
import begyyal.trading.constant.GameType;
import begyyal.trading.constant.Rule;
import begyyal.trading.object.BattleResult;
import begyyal.trading.object.RateRecord;

public class RateCalculator {

    private final PairList<BattleResult, RateRecord> records;

    public RateCalculator(PairList<BattleResult, RateRecord> records) {
	this.records = records;
    }

    public void exe(int startId) {

	int startIndex = records.indexOf(r -> r.v1.id == startId);
	var targetRange = this.records.subList(startIndex, this.records.size());

	var ratesByType = Arrays.stream(GameType.values())
	    .filter(type -> targetRange.stream().anyMatch(r -> r.v1.type == type))
	    .map(type -> this.calcRatesByType(type, startId))
	    .flatMap(m -> m == null ? Stream.empty() : m.entrySet().stream())
	    .collect(XMapGen.collect(e -> e.getKey(), e -> e.getValue()));

	var ratesByRule = Arrays.stream(Rule.values())
	    .filter(rule -> targetRange.stream().anyMatch(r -> r.v1.rule == rule))
	    .map(rule -> this.calcRatesByRule(rule, startId))
	    .flatMap(m -> m == null ? Stream.empty() : m.entrySet().stream())
	    .collect(XMapGen.collect(e -> e.getKey(), e -> e.getValue()));

	for (int i = this.adjustStartIndex(startIndex); i < this.records.size(); i++) {
	    var id = this.records.get(i).v1.id;
	    var rateRecord = this.records.get(i).v2;
	    if (ratesByType.containsKey(id))
		rateRecord.typeRate = ratesByType.get(id);
	    if (ratesByRule.containsKey(id))
		rateRecord.ruleRate = ratesByRule.get(id);
	    int winRate = (int) this.records
		.subList(i + 1 - FuncConst.maInterval, i + 1)
		.stream()
		.filter(r -> r.v1.isWin)
		.count() * 2;
	    rateRecord.totalRate = winRate;
	}
    }

    private int adjustStartIndex(int startIndex) {
	return startIndex < (FuncConst.maInterval - 1) ? (FuncConst.maInterval - 1) : startIndex;
    }

    private Map<Integer, Integer> calcRatesByType(GameType type, int ini) {
	return this.calcRates(ini, r -> r.type == type);
    }

    private Map<Integer, Integer> calcRatesByRule(Rule rule, int ini) {
	return this.calcRates(ini, r -> r.rule == rule);
    }

    private Map<Integer, Integer> calcRates(int startId, Predicate<BattleResult> pred) {

	var filtered = this.records.stream()
	    .map(r -> r.v1)
	    .filter(pred)
	    .collect(XListGen.collect());
	if (filtered.size() < FuncConst.maInterval)
	    return null;

	var rateMap = XMapGen.<Integer, Integer>newi();
	int startIndex = filtered.indexOf(r -> r.id >= startId);
	for (int i = this.adjustStartIndex(startIndex); i < filtered.size(); i++) {
	    int winRate = (int) filtered
		.subList(i + 1 - FuncConst.maInterval, i + 1)
		.stream()
		.filter(r -> r.isWin)
		.count() * 2;
	    rateMap.put(filtered.get(i).id, winRate);
	}

	return rateMap;
    }
}
