package begyyal.trading;

import java.io.IOException;

import begyyal.commons.object.collection.XMap.XMapGen;
import begyyal.trading.function.PropValidator;
import begyyal.trading.gui.StageConstructor;
import begyyal.trading.gui.constant.CallBackType;
import begyyal.trading.processor.PollingDispatcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Entrypoint extends Application {

    private PollingDispatcher pd;

    public static void main(String args[]) {
	if (PropValidator.exec())
	    launch(args);
    }

    @Override
    public void start(Stage stage) {
	try {
	    this.pd = PollingDispatcher.newi();
	    var cb = XMapGen.<CallBackType, Runnable>newi();
	    StageConstructor.newi(pd.run(cb), cb).process(stage);
	} catch (Exception e) {
	    System.out.println("[ERROR] Error occured in JavaFX app thread.");
	    e.getMessage();
	    e.printStackTrace();
	    Platform.exit();
	}
    }

    @Override
    public void stop() {
	try {
	    this.pd.close();
	} catch (IOException e) {
	    System.out.println("[ERROR] Error occured in the closing process.");
	    e.getMessage();
	    e.printStackTrace();
	}
    }
}