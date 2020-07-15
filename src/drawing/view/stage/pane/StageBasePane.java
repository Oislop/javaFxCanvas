package drawing.view.stage.pane;

import drawing.view.MainStage;
import javafx.scene.layout.VBox;

/**
 * stagePane 接口
 */
public interface StageBasePane {
    void createPane(MainStage mainStage);
    void initPane();
    VBox getPane();
}
