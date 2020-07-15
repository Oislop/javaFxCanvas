package drawing.utils;

import javafx.scene.text.Font;

import java.util.ArrayList;

public final class FontUtil {
    public static ArrayList getFontFamilies() {
        ArrayList<String> fontFamily = new ArrayList<>();
        for (int i = 3; i < Font.getFamilies().size(); i++) {
            String font = Font.getFamilies().get(i);
            if ((!font.contains("Arial") &&font.charAt(0) == 'A')
                    || (!font.contains("Consolas") &&font.charAt(0) == 'C')
                    || (!font.contains("Times") &&font.charAt(0) == 'T')
                    || (!font.contains("Microsoft") &&font.charAt(0) == 'M')
                    || (!font.contains("Segoe") &&font.charAt(0) == 'S')
                    || font.charAt(0) == 'B'
                    || font.charAt(0) == 'D'
                    || font.charAt(0) == 'E'
                    || font.charAt(0) == 'F'
                    || font.charAt(0) == 'G'
                    || font.charAt(0) == 'H'
                    || font.charAt(0) == 'I'
                    || font.charAt(0) == 'J'
                    || font.charAt(0) == 'K'
                    || font.charAt(0) == 'L'
                    || font.charAt(0) == 'N'
                    || font.charAt(0) == 'P'
                    || font.charAt(0) == 'R'
                    || font.charAt(0) == 'O'
                    || font.charAt(0) == 'V'
                    || font.charAt(0) == 'W'
                    || font.charAt(0) == 'Y'
                    || font.charAt(0) == 'Z') {
                continue;
            }
            fontFamily.add(font);
        }
        return fontFamily;
    }
}
