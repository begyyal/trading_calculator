package begyyal.trading.gui;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import begyyal.commons.object.collection.XList.XListGen;
import begyyal.commons.object.collection.XMap;
import begyyal.trading.gui.constant.CallBackType;
import begyyal.trading.gui.constant.GuiParts;
import begyyal.trading.gui.constant.WHConf;
import begyyal.trading.gui.object.DisplayDataBundle;
import begyyal.trading.gui.object.ProductState;
import begyyal.trading.gui.util.BDFieldConverter;
import begyyal.trading.market.constant.Commodity;
import begyyal.trading.market.constant.Fx;
import begyyal.trading.market.constant.Product;
import begyyal.trading.market.constant.StonkIndex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StageConstructor {
    private final DisplayDataBundle dataBundle;
    private final XMap<CallBackType, Runnable> callBack;
    private final int initWinHeight;
    private final int initWinWidth;

    private StageConstructor(
	DisplayDataBundle dataBundle,
	XMap<CallBackType, Runnable> callBack,
	int windowHeight,
	int windowWidth) {
	this.dataBundle = dataBundle;
	this.callBack = callBack;
	this.initWinHeight = windowHeight;
	this.initWinWidth = windowWidth;
    }

    public static StageConstructor newi(
	DisplayDataBundle dataBundle,
	XMap<CallBackType, Runnable> callBack) {
	var res = ResourceBundle.getBundle("common");
	var windowHeight = Integer.parseInt(res.getString("windowHeight"));
	var windowWidth = Integer.parseInt(res.getString("windowWidth"));
	return new StageConstructor(dataBundle, callBack, windowHeight, windowWidth);
    }

    public void process(Stage stage) {
	stage.setTitle("Trading Calculator");
	var pane = this.constructPane(stage);
	Scene scene = new Scene(pane, this.initWinWidth, this.initWinHeight);
	stage.setScene(scene);
	stage.show();
    }

    private GridPane constructPane(Stage stage) {

	var grid = new GridPane();
	grid.setVgap(5);
	grid.setHgap(10);
	grid.setPadding(new Insets(10, 10, 10, 10));

	// dialog window

	// index
	this.appendCategoryGrids(grid, "Stock Index", 0, StonkIndex.values());

	// FX
	this.appendCategoryGrids(grid, "FX", 1, Fx.values());

	// commodity
	this.appendCategoryGrids(grid, "Commodity", 2, Commodity.values());

	// result view
	var tic = new HBox();
	var ticl = new Label();
	ticl.textProperty().bind(this.dataBundle.tic.asString());
	tic.getChildren().addAll(this.newLeftLabel("Total if ceil"), ticl);

	var tif = new HBox();
	var tifl = new Label();
	tifl.textProperty().bind(this.dataBundle.tif.asString());
	tif.getChildren().addAll(this.newLeftLabel("Total if floor"), tifl);

	var rvg = new HBox();
	rvg.getChildren().addAll(tic, tif);
	rvg.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
	rvg.setAlignment(Pos.CENTER_LEFT);
	rvg.setSpacing(30);
	rvg.setPadding(new Insets(15, 15, 15, 15));
	grid.add(rvg, 0, 6, 1, 1);

	// control panel
	var grid6 = new HBox();
	var calc = new Button("Calculate");
	calc.setOnAction(e -> this.callBack.get(CallBackType.Calc).run());
	grid6.getChildren().addAll(calc);
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

    private <T extends Product> void appendCategoryGrids(
	GridPane parent,
	String cname,
	int i,
	T[] values) {

	var category = values[0].getCategory();
	var title1 = new Label(cname);
	title1.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
	parent.add(title1, 0, i * 2, 1, 1);

	this.dataBundle.productStates.get(category).forEach(this::createCategoryPane);
	var cbValues = FXCollections.observableArrayList(values);
	var dlg = this.createDialog(cbValues);

	var grid = new GridPane();
	var ad1 = new HBox();
	var btn = new Button("Add");
	btn.setOnAction(e -> {
	    var states = this.dataBundle.productStates.get(category);
	    cbValues.clear();
	    dlg.setResult(null);
	    Arrays.stream(values)
		.filter(v -> !states.stream()
		    .map(s -> s.key.type) // TODO
		    .collect(Collectors.toSet())
		    .contains(v))
		.forEach(cbValues::add);
	    var opt = dlg.showAndWait();
	    if (opt.isEmpty())
		return;
	    var result = opt.get();
	    var newState = new ProductState<Product>(result);
	    grid.add(this.createCategoryPane(newState), states.size(), 0);
	    states.add(newState);
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

    private Node createCategoryPane(ProductState<?> state) {

	var grid = new GridPane();
	grid.setPrefSize(WHConf.categoryHeight, WHConf.categoryWidth);
	grid.setVgap(3);
	grid.setPadding(new Insets(5, 5, 5, 5));
	grid.setBorder(GuiParts.plainBorder);

	var l = new Label(state.key.type.toString());
	l.setStyle("-fx-font-size: 14");
	grid.add(l, 0, 0, 2, 1);

	grid.add(this.newLeftLabel("quote"), 0, 1);
	var quote = new Label();
	quote.textProperty().bind(state.quote.asString());
	grid.add(quote, 1, 1);

	// *** input
	grid.add(this.newLeftLabel("ceil"), 0, 2);
	var ceil = this.newBDField();
	ceil.textProperty().bind(state.ceil.asString());
	grid.add(ceil, 1, 2);

	// *** input
	grid.add(this.newLeftLabel("floor"), 0, 3);
	var floor = this.newBDField();
	floor.textProperty().bind(state.floor.asString());
	grid.add(floor, 1, 3);

	// *** input
	grid.add(this.newLeftLabel("net pos"), 0, 4);
	var netPos = this.newBDField();
	netPos.textProperty().bind(state.netPos.asString());
	grid.add(netPos, 1, 4);

	grid.add(this.newLeftLabel("if ceil"), 0, 5);
	var ifceil = new Label();
	ifceil.textProperty().bind(state.ifCeil.asString());
	grid.add(ifceil, 1, 5);

	grid.add(this.newLeftLabel("if floor"), 0, 6);
	var iffloor = new Label();
	iffloor.textProperty().bind(state.ifFloor.asString());
	grid.add(iffloor, 1, 6);

	return grid;
    }

    private Label newLeftLabel(String str) {
	var l = new Label(str + " : ");
	l.setMinWidth(60);
	return l;
    }

    private TextField newBDField() {
	var field = new TextField();
	field.setTextFormatter(new TextFormatter<BigDecimal>(new BDFieldConverter()));
	return field;
    }
}
