package drawing.view.stage;

import drawing.view.MainStage;
import drawing.view.stage.pane.*;

/**
 * 功能区工厂
 */
public class StageFactory {
    /**
     * 静态工厂模式
     * @param type
     * @return
     */
    public static StageBasePane createPane(String type) {
        StageBasePane pane = null;
        if (type.equals("MenuBar")) {
            pane = new MenuBarPane();
        } else if (type.equals("ToolBar")) {
            pane = new ToolBarPane();
        } else if (type.equals("StatusBar")) {
            pane = new StatusBarPane();
        } else if (type.equals("BoardPane")) {
            pane = new BoardPane();
        }
        return pane;
    }
}
