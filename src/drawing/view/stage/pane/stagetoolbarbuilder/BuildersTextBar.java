package drawing.view.stage.pane.stagetoolbarbuilder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BuildersTextBar implements Builders {
    private VBox vBox;
    private Label label;

    @Override
    public void createBar() {
        vBox = new VBox();
        label = new Label();
    }

    @Override
    public void initBar() {
        label.setText("画 笔");
        label.setTextFill(Color.web("#0076a3"));
        label.setFont(new Font("Microsoft YaHei", 30));
        vBox.getChildren().add(label);
        vBox.setAlignment(Pos.CENTER);
    }

    @Override
    public VBox getBar() {
        return vBox;
    }

    public Label getLabel() {
        return label;
    }
}
