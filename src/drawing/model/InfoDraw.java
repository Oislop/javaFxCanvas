package drawing.model;

import javafx.scene.control.Label;

/**
 * 状态栏鼠标位置接口
 */

public interface InfoDraw {
    public static Label curLocation = new Label("");
    /**
     * 状态栏位置信息
     */
    public static void setText(String text){
        curLocation.setText(text);
    }

}
