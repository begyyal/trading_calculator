package begyyal.trading.object;

import begyyal.commons.object.collection.XMap;
import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.constant.GameType;
import begyyal.trading.constant.Rule;
import begyyal.trading.gui.constant.DispRule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Background;

public class DisplayDataBundle {
    public final XMap<Rule, PaneData> dataByRule;
    public final XMap<GameType, PaneData> dataByType;
    public final PaneData totalData;

    public DisplayDataBundle() {
	this.dataByRule = this.initMap(Rule.values());
	this.dataByType = this.initMap(GameType.values());
	this.totalData = new PaneData();
    }

    private <T> XMap<T, PaneData> initMap(T[] tarray) {
	var map = XMapGen.<T, PaneData>newi();
	for (var t : tarray)
	    map.put(t, new PaneData());
	return map;
    }

    public PaneData extractData(DispRule dr) {
	return dr.rule == null ? this.dataByType.get(dr.type) : this.dataByRule.get(dr.rule);
    }

    public class PaneData {
	public final ObservableList<Data<Number, Number>> data;
	public final ObservableList<Background> ppre;

	private PaneData() {
	    this.data = FXCollections.observableArrayList();
	    this.ppre = FXCollections.observableArrayList();
	}
    }
}