package drawing.view.stage.pane.stagetoolbarbuilder;

import javafx.scene.layout.VBox;

/**
 * 功能区builder模式
 */
public interface Builders {
    void createBar();
    void initBar();
    VBox getBar();
}
