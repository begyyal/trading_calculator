package begyyal.trading.processor;

import java.io.Closeable;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import begyyal.commons.constant.web.HttpHeader;
import begyyal.commons.constant.web.HttpStatus;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.commons.util.function.XUtils;
import begyyal.trading.constant.GameType;
import begyyal.trading.constant.Rule;
import begyyal.trading.db.ResultTableDao;
import begyyal.trading.gui.constant.GuiParts;
import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.market.constant.MarketDataDomain;
import begyyal.trading.market.object.MarketDataSet;
import begyyal.trading.market.processor.MarketDataAggregator;
import begyyal.trading.object.BattleResult;
import begyyal.trading.object.HttpBandle;
import begyyal.trading.object.ResultTable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Background;

public class Recorder implements Closeable {

    private final int intervalSec;
    private final String sessionId;
    private final HttpBandle<MarketDataDomain> httpBandle;
    private final MarketDataAggregator aggregator;
    private final ResultTableDao dao;
    private final ExecutorService exe;

    private Recorder() throws IOException {
	var res = ResourceBundle.getBundle("common");
	this.intervalSec = Integer.parseInt(res.getString("pollingIntervalSec"));
	this.sessionId = res.getString("iksm");
	this.httpBandle = new HttpBandle<MarketDataDomain>();
	this.aggregator = new MarketDataAggregator();
	this.dao = ResultTableDao.newi();
	this.exe = Executors.newSingleThreadExecutor(XUtils.createPlainThreadFactory("spla-po"));
    }

    public static Recorder newi() throws IOException {
	return new Recorder();
    }

    public DisplayDataBundle run() throws Exception {

	var mktdata = new MarketDataSet();
	this.aggregator.fill(mktdata);

	// 1 マーケットデータセットにデータをフィル
	// 2 記録

	// ポーリングスレッド起動(1/2)

	// ディスプレイデータに変換

	// this.httpBandle.setReq(TradingEconomicsApi.RESULTS, this.createReq());
	// var res = this.httpBandle.send(TradingEconomicsApi.RESULTS, BodyHandlers.ofString());
	// var status = HttpStatus.parse(res.statusCode());
	// if (status.getCategory() != 2)
	// throw new Exception("Http status by the API is not success. status:" + status.code);
	// var dataBundle = new DisplayDataBundle();
	// this.record(res.body(), dataBundle);
	// this.exe.execute(() -> {
	// while (XUtils.sleep(1000 * intervalSec))
	// this.process(dataBundle);
	// });
	// return dataBundle;
	return new DisplayDataBundle();
    }

//    private void process(DisplayDataBundle dataBundle) {
//	this.httpBandle.sendAsync(MarketDataDomain.RESULTS, BodyHandlers.ofString())
//	    .thenApply(HttpResponse::body)
//	    .thenAccept(j -> this.record(j, dataBundle))
//	    .join();
//    }
//
//    private HttpRequest createReq() {
//	return HttpRequest.newBuilder()
//	    .uri(MarketDataDomain.RESULTS.toURI())
//	    .header(HttpHeader.Cookie.str, "iksm_session=" + this.sessionId)
//	    .build();
//    }

    private void record(String json, DisplayDataBundle dataBundle) {

	ResultTable table = null;
	try {
	    table = this.dao.read();
	} catch (IOException e) {
	    System.out.println("[ERROR] IOException caused when the dao reading.");
	    e.printStackTrace();
	    return;
	}

	JsonNode tree = null;
	try {
	    tree = new ObjectMapper().readTree(json);
	} catch (IOException e1) {
	    System.out.println("[ERROR] IOException caused when reading json.");
	    e1.printStackTrace();
	    return;
	}
	var list = XListGen.<BattleResult>newi();

	for (JsonNode jn : tree.get("results")) {
	    var type = GameType.parse(jn.get("type").asText());
	    if (type == null)
		continue;
	    var rule = Rule.parse(jn.get("rule").get("key").asText());
	    if (rule == null)
		continue;
	    var battleNum = jn.get("battle_number").asInt();
	    var isWin = "victory".equals(jn.get("my_team_result").get("key").asText());
	    int weaponId = jn.get("player_result").get("player").get("weapon").get("id").asInt();
	    list.add(new BattleResult(battleNum, isWin, type, rule, weaponId));
	}

	try {
	    this.dao.write(table);
	} catch (IOException e) {
	    System.out.println("[ERROR] IOException caused when the dao writing.");
	    e.printStackTrace();
	    return;
	}
    }

    private Data<Number, Number> createDataPoint(int x, int y) {
	return new XYChart.Data<Number, Number>(x, y);
    }

    private Background distinguishBkg(Boolean isWin) {
	return isWin == null ? GuiParts.bkgWhite : isWin ? GuiParts.bkgRed : GuiParts.bkgBlue;
    }

    @Override
    public void close() throws IOException {
	this.exe.shutdown();
	this.exe.shutdownNow();
    }
}
