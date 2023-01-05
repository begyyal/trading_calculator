package begyyal.trading.object;

import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import begyyal.commons.constant.Strs;
import begyyal.commons.object.Pair;
import begyyal.commons.object.collection.PairList;
import begyyal.commons.object.collection.PairList.PairListGen;
import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.trading.constant.FuncConst;
import begyyal.trading.constant.GameType;
import begyyal.trading.constant.Rule;
import begyyal.trading.processor.RateCalculator;

public class ResultTable {

    private final PairList<BattleResult, RateRecord> records;

    private ResultTable(PairList<BattleResult, RateRecord> records) {
	this.records = records;
    }

    public static ResultTable of(List<String> plainLines) {
	var records = plainLines.stream()
	    .map(ResultTable::deserializeLine)
	    .collect(PairListGen.collect());
	return new ResultTable(records);
    }

    public static Pair<BattleResult, RateRecord> deserializeLine(String pl) {
	var st = new StringTokenizer(pl, Strs.comma);
	var battleNum = Integer.parseInt(st.nextToken());
	var isWin = "1".equals(st.nextToken());
	var type = GameType.parse(Integer.parseInt(st.nextToken()));
	var rule = Rule.parse(Integer.parseInt(st.nextToken()));
	int weaponId = Integer.parseInt(st.nextToken());
	return Pair.of(
	    new BattleResult(battleNum, isWin, type, rule, weaponId),
	    new RateRecord(
		Integer.parseInt(st.nextToken()),
		Integer.parseInt(st.nextToken()),
		Integer.parseInt(st.nextToken())));
    }

    public List<String> serialize() {
	return this.records.stream()
	    .map(this::serializeRecord)
	    .collect(Collectors.toList());
    }

    public String serializeRecord(Pair<BattleResult, RateRecord> p) {
	var vArray = new Object[] {
		p.v1.id,
		p.v1.isWin ? 1 : 0,
		p.v1.type.id,
		p.v1.rule.id,
		p.v1.weaponId,
		p.v2.ruleRate,
		p.v2.typeRate,
		p.v2.totalRate };
	return String.join(Strs.comma, (String[])vArray);
    }

    public boolean integrate(XList<BattleResult> results) {
	if (results.isEmpty())
	    return false;
	int latestId = this.records.getTip().v1.id;
	var startIndexOfNewRange = results.indexOf(r -> r.id == latestId) + 1;
	if (startIndexOfNewRange == 0)
	    this.merge(results);
	else if (startIndexOfNewRange < results.size())
	    this.merge(results.createPartialList(startIndexOfNewRange, results.size()));
	else
	    return false;
	return true;
    }

    private void merge(XList<BattleResult> results) {
	this.records.addAll(results.stream()
	    .map(r -> Pair.of(r, new RateRecord()))
	    .collect(PairListGen.collect()));
	new RateCalculator(this.records).exe(results.get(0).id);
    }

    public XList<Integer> getWinRates(GameType type) {
	return this.getWinRates(type, null, t -> t.typeRate);
    }

    public XList<Integer> getWinRates(Rule rule) {
	return this.getWinRates(null, rule, t -> t.ruleRate);
    }

    public XList<Integer> getTotalWinRates() {
	return this.getWinRates(null, null, t -> t.totalRate);
    }

    private XList<Integer> getWinRates(
	GameType type,
	Rule rule,
	Function<RateRecord, Integer> extractor) {
	return this.records.stream()
	    .filter(t -> resultIsTarget(t.v1, type, rule))
	    .map(t -> extractor.apply(t.v2))
	    .filter(r -> r >= 0)
	    .collect(XListGen.collect());
    }

    public XList<Boolean> getTruncationRange(GameType type) {
	return this.getTruncationRange(type, null);
    }

    public XList<Boolean> getTruncationRange(Rule rule) {
	return this.getTruncationRange(null, rule);
    }

    public XList<Boolean> getTotalTruncationRange() {
	return this.getTruncationRange(null, null);
    }

    private XList<Boolean> getTruncationRange(GameType type, Rule rule) {
	var filtered = this.records.stream()
	    .map(p -> p.v1)
	    .filter(r -> resultIsTarget(r, type, rule))
	    .collect(XListGen.collect());
	return IntStream
	    .range(filtered.size() - FuncConst.maInterval,
		filtered.size() - FuncConst.maInterval + FuncConst.ppreCount)
	    .mapToObj(i -> i < 0 ? null : filtered.get(i).isWin)
	    .collect(XListGen.collect());
    }

    private static boolean resultIsTarget(BattleResult r, GameType type, Rule rule) {
	return type != null ? r.type == type
		: (rule == null || r.rule == rule);
    }
}
