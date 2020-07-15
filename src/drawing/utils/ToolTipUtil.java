package drawing.utils;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 提示控制类
 */
public class ToolTipUtil {
    public static void setTipTime(Tooltip tooltip, int time){
        try {
            Class tipClass = tooltip.getClass();
            Field f = tipClass.getDeclaredField("BEHAVIOR");
            f.setAccessible(true);
            Class behavior = Class.forName("javafx.scene.control.Tooltip$TooltipBehavior");
            Constructor constructor =
                    behavior.getDeclaredConstructor(Duration.class,
                            Duration.class,
                            Duration.class,
                            boolean.class);
            constructor.setAccessible(true);
            f.set(behavior, constructor.newInstance(
                    new Duration(100),
                    new Duration(time),
                    new Duration(100), false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
