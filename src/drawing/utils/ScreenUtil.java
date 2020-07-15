package drawing.utils;


import drawing.config.SizeEnum;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * 设置界面大小类
 */
public class ScreenUtil {

    public static void setScreen() {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();

        double widthper = 0.73;
        double heightper = 0.83;

        double canvansWidPer = 0.6502; // 911
        double canvansHeiPer = 0.86; // 814

        SizeEnum.STAGE_WIDTH.setValue((int) (width * widthper)); // 1401
        SizeEnum.STAGE_HEIGHT.setValue((int) (height * heightper)); //896

        // System.out.println((int) (width * widthper) + " " +
        //         (int) (height * heightper));

        SizeEnum.CANVAS_HEIGHT.setValue((int) (SizeEnum.STAGE_HEIGHT.getValue() * canvansHeiPer));
        SizeEnum.CANVAS_WIDTH.setValue((int) (SizeEnum.STAGE_WIDTH.getValue() * canvansWidPer));

    }

    public static double getScreenY() {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double height = screenRectangle.getHeight() / 2;
        return height;
    }

}
