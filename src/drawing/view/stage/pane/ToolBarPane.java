package drawing.view.stage.pane;

import drawing.view.MainStage;
import drawing.view.stage.pane.stagetoolbarbuilder.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ToolBarPane implements StageBasePane{

    private MainStage stage;
    private VBox vBox;

    private VBox textBarVbox;
    private VBox colorBarVbox;
    private VBox detailBarVbox;
    private VBox toolBarVbox;
    private VBox layersVbox;

    private static BuildersLayersBar layersBar;

    @Override
    public void createPane(MainStage mainStage) {
        this.stage = mainStage;
        initVBox();
        initPane();
    }

    public static BuildersLayersBar getLayersBar() {
        return layersBar;
    }

    @Override
    public void initPane() {

        Director manager = new Director();
        BuildersTextBar textBar = new BuildersTextBar();
        BuildersColorBar colorBar = new BuildersColorBar();
        BuildersDetailBar detailBar = new BuildersDetailBar();
        BuildersToolBar toolBar = new BuildersToolBar();
        layersBar = new BuildersLayersBar();

        manager.createToolBar(textBar);
        manager.createToolBar(colorBar);
        manager.createToolBar(detailBar);
        manager.createToolBar(toolBar);
        manager.createToolBar(layersBar);

        detailBar.setColorBar(colorBar);
        toolBar.setDetailBar(detailBar);
        toolBar.setTextBar(textBar);
        //layersBar.setBoardPane(stage.getBoard());

        textBarVbox = textBar.getBar();
        colorBarVbox = colorBar.getBar();
        detailBarVbox = detailBar.getBar();
        toolBarVbox = toolBar.getBar();
        layersVbox = layersBar.getBar();


        textBarVbox.setPadding(new Insets(5, 15, 0, 5));
        toolBarVbox.setPadding(new Insets(0, 5, 50, 10));
        //detailBarVbox.setPadding(new Insets(0,10,60,15));
        colorBarVbox.setPadding(new Insets(10, 5, 20, 10));
        layersVbox.setPadding(new Insets(0,5,5,10));

        vBox.getChildren().add(textBarVbox);
        vBox.getChildren().add(toolBarVbox);
        vBox.getChildren().add(detailBarVbox);
        vBox.getChildren().add(colorBarVbox);
        vBox.getChildren().add(layersVbox);
    }

    @Override
    public VBox getPane() {
        return vBox;
    }

    private void initVBox() {
        vBox = new VBox();
        vBox.setSpacing(12);
        vBox.setStyle("-fx-background-color: #f0f2f3");
        vBox.setAlignment(Pos.CENTER);
        vBox.prefHeightProperty().bind(stage.heightProperty());
    }
}
