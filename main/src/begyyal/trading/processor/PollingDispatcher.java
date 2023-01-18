package begyyal.trading.processor;

import java.io.Closeable;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import begyyal.commons.object.collection.XMap;
import begyyal.commons.util.function.XUtils;
import begyyal.trading.db.TCDao;
import begyyal.trading.db.object.TCTable;
import begyyal.trading.gui.constant.CallBackType;
import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.market.object.MarketDataSet;
import begyyal.trading.market.processor.MarketDataAggregator;

public class PollingDispatcher implements Closeable {

    private final int intervalSec;
    private final MarketDataAggregator aggregator;
    private final TCDao dao;
    private final TCTable table;
    private final ExecutorService exe;

    private PollingDispatcher(TCDao dao, TCTable table) throws IOException {
	var res = ResourceBundle.getBundle("common");
	this.intervalSec = Integer.parseInt(res.getString("pollingIntervalSec"));
	this.aggregator = new MarketDataAggregator();
	this.exe = Executors.newSingleThreadExecutor(XUtils.createPlainThreadFactory("bg-tc-po"));
	this.dao = dao;
	this.table = table;
    }

    public static PollingDispatcher newi() throws IOException {
	var dao = TCDao.newi();
	TCTable table;
	try {
	    table = dao.read();
	} catch (IOException e) {
	    table = TCTable.newi();
	    System.out.println("[ERROR] IOException caused when the dao reading.");
	    e.printStackTrace();
	}
	return new PollingDispatcher(dao, table);
    }

    public DisplayDataBundle run(XMap<CallBackType, Runnable> cb) throws Exception {
	var dataBundle = DisplayDataBundle.of(table);
	//this.process(dataBundle);
//	this.exe.execute(() -> {
//	    while (XUtils.sleep(1000 * intervalSec))
//		this.process(dataBundle);
//	});
	cb.put(CallBackType.Calc, () -> this.process(dataBundle));
	return dataBundle;
    }

    private void process(DisplayDataBundle dataBundle) {
	var mktdata = new MarketDataSet();
	this.aggregator.fill(mktdata);
	PLCalculator.exe(mktdata, dataBundle);
    }

    @Override
    public void close() throws IOException {
	this.exe.shutdown();
	this.exe.shutdownNow();
	// TODO confirm
	// TODO convert dispdata to table
	try {
	    this.dao.write(this.table);
	} catch (IOException e) {
	    System.out.println("[ERROR] IOException caused when the dao writing.");
	    e.printStackTrace();
	}
    }
}
