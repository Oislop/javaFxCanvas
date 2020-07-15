package drawing.view.stage.pane.stagetoolbarbuilder;

import drawing.model.InfoDraw;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BuildersStatusBar implements Builders, InfoDraw {
    private HBox hbox;
    private VBox vBox;

    @Override
    public void createBar() {
        hbox = new HBox();
        vBox = new VBox();
    }

    @Override
    public void initBar() {
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefWidth(817);
        hbox.setPadding(new Insets(5, 300, 5, 15));
        curLocation.setFont(Font.font("Microsoft YaHei", 16));
        hbox.getChildren().add(curLocation);
        vBox.getChildren().add(hbox);
    }

    @Override
    public VBox getBar() {
        return vBox;
    }
}
