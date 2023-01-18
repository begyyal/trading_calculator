package begyyal.trading;

import java.io.IOException;

import begyyal.trading.function.PropValidator;
import begyyal.trading.gui.StageConstructor;
import begyyal.trading.processor.Recorder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Entrypoint extends Application {

    private Recorder rec;

    public static void main(String args[]) {
	if (PropValidator.exec())
	    launch(args);
    }

    @Override
    public void start(Stage stage) {
	try {
	    this.rec = Recorder.newi();
	    StageConstructor.newi(rec.run()).process(stage);
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
	    this.rec.close();
	} catch (IOException e) {
	    System.out.println("[ERROR] Error occured in the closing process.");
	    e.getMessage();
	    e.printStackTrace();
	}
    }
}