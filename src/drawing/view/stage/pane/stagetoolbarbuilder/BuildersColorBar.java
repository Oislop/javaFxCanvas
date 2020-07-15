package drawing.view.stage.pane.stagetoolbarbuilder;

import drawing.config.PngPathEnum;
import drawing.config.ShapeProperty;
import drawing.config.SizeEnum;
import drawing.model.StageToolButton;
import drawing.utils.ToolTipUtil;
import drawing.utils.getcolor.ScreenCapture;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BuildersColorBar implements Builders {

    private VBox vBox;
    private TilePane tilePane;
    private Label colorLabel;
    private GridPane gridPane;

    /**
     * 拾色器
     */
    private Button colorSelectButton;

    /**
     * 拾色器类
     */
    private ScreenCapture screenCapture;
    BufferedImage desktopImg;

    /**
     * 初始化颜色为黑色
     */
    private Color presentColor = Color.BLACK;

    /**
     * 颜色选择器
     */
    private ColorPicker colorPicker;

    /**
     * 颜色选择list
     */
    private List<StageToolButton> colorButton = new ArrayList<>();

    /**
     * 颜色选择list填充颜色
     */
    private final String[] colors = new String[]{

            //         "\"#ffffff\"", "\"#c3c3c3\"", "\"#585858\"", "\"#000000\"", "\"#88001b\"", "\"#ec1c24\"",
            //         "\"#ff7f27\"", "\"#ffca18\"", "\"#fdeca6\"", "\"#fff200\"", "\"#c4ff0e\"", "\"#0ed145\"",
            //         "\"#8cfffb\"", "\"#f2f2f2\"", "\"#3f48cc\"", "\"#b83dba\"", "\"#ffaec8\"", "\"#b97a56\""
            "#ffffff", "#c3c3c3", "#585858", "#000000", "#88001b", "#ec1c24",
            "#ff7f27", "#ffca18", "#fdeca6", "#fff200", "#c4ff0e", "#0ed145",
            "#8cfffb", "#f2f2f2", "#3f48cc", "#b83dba", "#ffaec8", "#b97a56"
    };

    @Override
    public void createBar() {
        vBox = new VBox();
        colorLabel = new Label();
        colorPicker = new ColorPicker();
    }

    @Override
    public void initBar() {
        intiShowColor();
        initColorBar();
    }

    @Override
    public VBox getBar() {
        return vBox;
    }

    private void intiShowColor() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 5, 10, 5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        colorLabel.setPrefSize(295, 40);

        colorPicker.setPrefSize(200, 40);
        colorPicker.setValue(Color.BLACK);
        //colorPicker.getStyleClass().add("button");
        String color = "#" + colorPicker.getValue().toString().substring(2, 8);
        colorLabel.setStyle("-fx-background-color:" + color + ";");

        colorPicker.setOnAction((ActionEvent t) -> {
            String color1 = "#" + colorPicker.getValue().toString().substring(2, 8);
            //System.out.println(color1);
            colorLabel.setStyle("-fx-background-color:" + color1 + ";");
            presentColor = colorPicker.getValue();
            ShapeProperty.setColor(presentColor);
        });

        Image ImgColorPicker = new Image(PngPathEnum.COLORPICKER.toString());
        ImageView imageView = new ImageView(ImgColorPicker);
        imageView.setFitHeight(25);
        imageView.setFitWidth(22);
        Tooltip tooltip = new Tooltip("拾色器");
        ToolTipUtil.setTipTime(tooltip, 5000);
        colorSelectButton = new Button();
        colorSelectButton.setPrefSize(90, 40);
        colorSelectButton.setTooltip(tooltip);
        colorSelectButton.setGraphic(imageView);
        colorSelectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cap();
            }
        });

        gridPane.add(colorLabel, 0, 0, 2, 1);
        gridPane.add(colorPicker, 0, 1);
        gridPane.add(colorSelectButton, 1, 1);

        vBox.getChildren().add(gridPane);
    }

    /**
     * 拾色器处理入口
     */
    private void cap() {
        try {
            Thread.sleep(200L);
            captureDesktop();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (this.screenCapture == null)
            this.screenCapture = new ScreenCapture(colorPicker, colorLabel, desktopImg);
        else
            this.screenCapture.refreshBackGround(colorPicker, colorLabel, desktopImg);
    }

    public void captureDesktop() throws Exception {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(d);
        this.desktopImg = new BufferedImage((int) d.getWidth(),
                (int) d.getHeight(), 6);
        GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        Robot robot = new Robot(device);
        this.desktopImg = robot.createScreenCapture(rect);
    }

    private void initColorBar() {
        tilePane = new TilePane();
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(new Insets(5, 5, 10, 2));
        tilePane.setPrefColumns(6);
        tilePane.setHgap(5);
        tilePane.setVgap(5);

        DropShadow shadow = new DropShadow();
        for (int i = 0; i < colors.length; i++) {
            StageToolButton cButton = new StageToolButton(colors[i]);
            cButton.setStyle("-fx-base: " + colors[i] + ";");
            cButton.setPrefSize(SizeEnum.COLOR_BUTTON_WIDTH.getValue(), SizeEnum.COLOR_BUTTON_HEIGHT.getValue());
            cButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                cButton.setEffect(shadow);
            });

            cButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                cButton.setEffect(null);
            });

            cButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                presentColor = Color.web(((StageToolButton) e.getSource()).getName());
                colorPicker.setValue(presentColor);
                String color1 = "#" + colorPicker.getValue().toString().substring(2, 8);
                colorLabel.setStyle("-fx-background-color:" + color1 + ";");
                ShapeProperty.setColor(presentColor);
            });
            colorButton.add(cButton);
        }
        tilePane.getChildren().addAll(colorButton);
        vBox.getChildren().add(tilePane);

    }

    public void resetColor() {
        colorPicker.setValue(Color.BLACK);
        String color = "#" + colorPicker.getValue().toString().substring(2, 8);
        colorLabel.setStyle("-fx-background-color:" + color + ";");
    }

}
