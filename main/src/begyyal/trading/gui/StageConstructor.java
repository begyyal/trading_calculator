package begyyal.trading.gui;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import begyyal.commons.constant.Stonk;
import begyyal.commons.constant.Strs;
import begyyal.commons.object.collection.XList.XListGen;
import begyyal.trading.gui.constant.DispGameType;
import begyyal.trading.gui.constant.DispRule;
import begyyal.trading.gui.constant.GuiParts;
import begyyal.trading.gui.constant.PaneState;
import begyyal.trading.gui.constant.WHConf;
import begyyal.trading.gui.object.ComponentPalette;
import begyyal.trading.gui.object.DispPaneData;
import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.gui.object.DisplayedState;
import begyyal.trading.market.constant.CcyPair;
import begyyal.trading.market.constant.Commodity;
import begyyal.trading.object.CategoryState;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StageConstructor {

    private static final DispGameType initType = DispGameType.GACHI;
    private static final DispRule initRule = DispRule.GACHI_ALL;

    private final DisplayDataBundle dataBundle;
    private final Integer[] term;
    private final int initWinHeight;
    private final int initWinWidth;

    private final DisplayedState current;

    private StageConstructor(
	Integer[] term,
	int windowHeight,
	int windowWidth,
	DisplayDataBundle dataBundle) {
	this.term = term;
	this.initWinHeight = windowHeight;
	this.initWinWidth = windowWidth;
	this.dataBundle = dataBundle;
	this.current = new DisplayedState();
    }

    public static StageConstructor newi(DisplayDataBundle dataBundle) {

	var res = ResourceBundle.getBundle("common");
	var term = Arrays.stream(res.getString("term").split(Strs.comma))
	    .map(Integer::parseInt)
	    .toArray(Integer[]::new);
	Arrays.sort(term);
	var windowHeight = Integer.parseInt(res.getString("windowHeight"));
	var windowWidth = Integer.parseInt(res.getString("windowWidth"));

	return new StageConstructor(term, windowHeight, windowWidth, dataBundle);
    }

    public void process(Stage stage) {

	stage.setTitle("Trading Calculator");

	var typeOpt = FXCollections.observableArrayList(DispGameType.values());
	var ruleOpt = initType.type == null
		? FXCollections.<DispRule>observableArrayList()
		: FXCollections.observableArrayList(DispRule.getBy(initType.type));
	var termOpt = FXCollections.observableArrayList(term);
	var palette = new ComponentPalette(typeOpt, ruleOpt, termOpt);

	var resolver = new SetupResolver(palette, this.initWinHeight, this.initWinWidth);
//	resolver.setupPaneData(dataBundle);
//	resolver.setupPpre();
//	resolver.setupLineChart();
//	resolver.setupCurrentRate(this.current.rate);
//
//	this.addListenersTo(palette);
//	this.setDefaultValuesTo(palette);

	var pane = this.constructPane(palette, stage);
	Scene scene = new Scene(pane, this.initWinWidth, this.initWinHeight);
	stage.setScene(scene);
	stage.show();
    }

    private void addListenersTo(ComponentPalette palette) {

	palette.typeCombo.valueProperty().addListener(
	    (obs, o, n) -> {
		this.current.state.set(PaneState.parse(n.rule));
		palette.updateChart(n);
		palette.updateRuleComboItems(n);
		palette.updatePpre(n);
	    });

	palette.ruleCombo.valueProperty().addListener(
	    (obs, o, n) -> {
		// 間接的な更新は全てスルー。明示的に値から値へ変えたときのみ
		if (o != null && n != null && o.type == n.type) {
		    this.current.state.set(PaneState.parse(n));
		    palette.updateChart(n);
		    palette.updatePpre(n);
		}
	    });

	palette.termCombo.valueProperty().addListener(
	    (obs, o, n) -> palette.xAxis.setLowerBound(-n + 1));

	palette.dispDataMap.entrySet().stream()
	    .forEach(e -> this.addListenersToDpd(e.getKey(), e.getValue(), palette));

	this.current.state.addListener((obs, o, n) -> {
	    this.updateCurrentRate(palette.getDpd(this.current.state.get()).series.getData());
	});
    }

    private void addListenersToDpd(PaneState s, DispPaneData dpd, ComponentPalette palette) {
	dpd.series.getData().addListener((Change<? extends XYChart.Data<Number, Number>> c) -> {
	    if (changeIsTarget(c, c2 -> c2.wasReplaced(), s))
		this.updateCurrentRate(c.getList());
	});
	dpd.ppre.addListener((Change<? extends Background> c) -> {
	    if (changeIsTarget(c, c2 -> c2.wasReplaced(), s))
		palette.updatePpre(c.getList());
	});
    }

    private void updateCurrentRate(ObservableList<? extends XYChart.Data<Number, Number>> rates) {
	if (!rates.isEmpty()) {
	    int r = (int) rates.get(rates.size() - 1).getYValue();
	    this.current.rate.set(Integer.toString(r));
	} else
	    this.current.rate.set(Strs.empty);
    }

    private <T> boolean changeIsTarget(Change<T> c, Predicate<Change<T>> cp, PaneState s) {
	while (c.next())
	    if (cp.test(c) && s == this.current.state.get())
		return true;
	return false;
    }

    private void setDefaultValuesTo(ComponentPalette palette) {
	// palette.typeCombo.setValue(initType);
	palette.ruleCombo.setValue(initRule);
	palette.termCombo.setValue(this.term[0]);
    }

    private GridPane constructPane(ComponentPalette palette, Stage stage) {

	var grid = new GridPane();
	grid.setVgap(5);
	grid.setHgap(10);
	grid.setPadding(new Insets(10, 10, 10, 10));

	// dialog window

	// index
	this.appendCategoryGrids(grid, "Stock Index", 0, Stonk.values());

	// FX
	this.appendCategoryGrids(grid, "FX", 1, CcyPair.values());

	// commodity
	this.appendCategoryGrids(grid, "Commodity", 2, Commodity.values());

	// result view
	var rvg = new HBox();
	var tic = new HBox();
	tic.getChildren().addAll(
	    this.newLeftLabel("Total if ceil"),
	    new Label("10,000.0"));
	var tif = new HBox();
	tif.getChildren().addAll(
	    this.newLeftLabel("Total if floor"),
	    new Label("10,000.0"));
	rvg.getChildren().addAll(tic, tif);
	rvg.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
	rvg.setAlignment(Pos.CENTER_LEFT);
	rvg.setSpacing(30);
	rvg.setPadding(new Insets(15, 15, 15, 15));
	grid.add(rvg, 0, 6, 1, 1);

	// control panel
	var grid6 = new HBox();
	grid6.getChildren().addAll(
	    new Button("Calculate"));
	grid6.setAlignment(Pos.CENTER_RIGHT);
	grid6.setPadding(new Insets(0, 10, 0, 10));
	grid.add(grid6, 1, 6, 1, 1);

	return grid;
    }

    private <T> Dialog<T> createDialog(ObservableList<T> values) {
	var dialog = new Dialog<T>();
	dialog.setTitle("Add Category");
	var combo = new ComboBox<T>(values);
	dialog.getDialogPane().setContent(combo);
	dialog.getDialogPane().getButtonTypes().addAll(
	    ButtonType.OK,
	    ButtonType.CANCEL);
	var btns = dialog.getDialogPane().getChildren().stream()
	    .flatMap(n -> (n instanceof ButtonBar)
		    ? ((ButtonBar) n).getButtons().stream()
			.filter(n2 -> !((Button) n2).isCancelButton())
		    : Stream.empty())
	    .map(n -> {
		n.setDisable(true);
		return n;
	    })
	    .collect(XListGen.collect());
	combo.valueProperty().addListener((obs, o, n) -> {
	    if (n != null && o != n) {
		btns.forEach(b -> b.setDisable(false));
		// TODO validate
	    }
	});
	dialog.setResultConverter(bt -> bt == ButtonType.OK ? combo.getValue() : null);
	return dialog;
    }

    private <T extends Enum<T>> void appendCategoryGrids(
	GridPane parent,
	String cname,
	int i,
	T[] values) {

	var title1 = new Label(cname);
	title1.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
	parent.add(title1, 0, i * 2, 1, 1);

	var cbValues = FXCollections.observableArrayList(values);
	var dlg = this.createDialog(cbValues);

	var grid = new GridPane();
	var ad1 = new HBox();
	var btn = new Button("Add");
	btn.setOnAction(e -> {
	    var states = this.dataBundle.getCategoryStates(values[0].getClass());
	    cbValues.clear();
	    dlg.setResult(null);
	    Arrays.stream(values)
		.filter(v -> !states.stream()
		    .map(s -> s.category)
		    .collect(Collectors.toSet())
		    .contains(v))
		.forEach(cbValues::add);
	    var opt = dlg.showAndWait();
	    if (opt.isEmpty())
		return;
	    var result = opt.get();
	    grid.add(this.createCategoryPane(result), states.size(), 0);
	    states.add(CategoryState.newi(result));
	});
	ad1.getChildren().addAll(btn);
	ad1.setAlignment(Pos.CENTER_RIGHT);
	ad1.setPadding(new Insets(0, 10, 0, 10));
	parent.add(ad1, 1, i * 2, 1, 1);

	grid.setHgap(10);
	grid.setPrefSize(this.initWinWidth, WHConf.categoryHeight);
	grid.setBackground(GuiParts.bkgBlue);
	parent.add(grid, 0, i * 2 + 1, 2, 1);
    }

    private <T extends Enum<T>> Node createCategoryPane(T category) {
	var grid = new GridPane();
	grid.setPrefSize(WHConf.categoryHeight, WHConf.categoryWidth);
	grid.setVgap(3);
	grid.setPadding(new Insets(5, 5, 5, 5));
	grid.setBorder(GuiParts.plainBorder);
	var l = new Label(category.toString());
	l.setStyle("-fx-font-size: 14");
	grid.add(l, 0, 0, 2, 1);
	grid.add(this.newLeftLabel("quote"), 0, 1);
	grid.add(new Label("1000.0"), 1, 1);
	grid.add(this.newLeftLabel("ceil"), 0, 2);
	grid.add(new Label("2000.0"), 1, 2);
	grid.add(this.newLeftLabel("floor"), 0, 3);
	grid.add(new Label("500.0"), 1, 3);
	grid.add(this.newLeftLabel("net pos"), 0, 4);
	grid.add(new Label("1.0"), 1, 4);
	grid.add(this.newLeftLabel("if ceil"), 0, 5);
	grid.add(new Label("50,000.0"), 1, 5);
	grid.add(this.newLeftLabel("if floor"), 0, 6);
	grid.add(new Label("-20,000.0"), 1, 6);
	return grid;
    }

    private Label newLeftLabel(String str) {
	return new Label(str + " : ");
    }
}
