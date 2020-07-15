package drawing.model;


import javafx.scene.control.Button;

public class StageToolButton extends Button {
    String name;

    public StageToolButton(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
