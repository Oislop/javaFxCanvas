package drawing.utils;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public final class ShadowUtil {
    private static DropShadow dropShadow = new DropShadow();
    public static DropShadow setShadow() {
        dropShadow.setColor(Color.web("#ff3b30"));
        dropShadow.setHeight(25);
        dropShadow.setWidth(25);
        dropShadow.setSpread(0.35);
        return dropShadow;
    }
}
