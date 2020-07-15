package drawing.model;

import javafx.scene.shape.Shape;

public class Context {

    private Shape myShape;

    public Context(Shape myShape){
        this.myShape = myShape;
    }

    public Shape executeShape() {
        return myShape;
    }

}
