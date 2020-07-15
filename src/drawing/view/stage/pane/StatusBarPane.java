package drawing.view.stage.pane;

import drawing.model.InfoDraw;
import drawing.view.MainStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StatusBarPane implements StageBasePane, InfoDraw {
    private HBox hbox;
    private VBox vBox;

    @Override
    public void createPane(MainStage mainStage) {
        hbox = new HBox();
        vBox = new VBox();
        initPane();
    }

    @Override
    public void initPane() {
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefWidth(817);
        hbox.setPadding(new Insets(5, 300, 5, 15));
        curLocation.setFont(Font.font("Microsoft YaHei", 16));
        hbox.getChildren().add(curLocation);
        vBox.getChildren().add(hbox);
    }

    @Override
    public VBox getPane() {
        return vBox;
    }
}
