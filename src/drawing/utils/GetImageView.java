package drawing.utils;


import drawing.config.SizeEnum;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class GetImageView {
    public static ImageView getImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(SizeEnum.TOOLBAR_BUTTON_WIDTH.getValue());
        imageView.setFitWidth(SizeEnum.TOOLBAR_BUTTON_HEIGHT.getValue());
        return imageView;
    }
}
