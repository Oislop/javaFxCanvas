package drawing;

import drawing.config.SizeEnum;
import drawing.view.MainStage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainStage.getInstance();
        // new MainStage();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

