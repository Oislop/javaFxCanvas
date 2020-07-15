package drawing.view.stage.pane.stagetoolbarbuilder;


import drawing.config.PngPathEnum;
import drawing.model.MyPen;
import drawing.utils.GetImageView;
import drawing.utils.ToolTipUtil;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;


public class BuildersLayersBar implements Builders {

    private VBox vBox;

    private VBox contentVBox;

    private Label tipLabel;

    private ScrollPane scrollPane;

    private Group boradPaneGroup = new Group();

    private ArrayList<Tooltip> tooltipsList = new ArrayList<>();

    private static LayersMenuPane layersMenuPane = LayersMenuPane.getInstance();

    @Override
    public void createBar() {
        vBox = new VBox();
        contentVBox = new VBox();
        scrollPane = new ScrollPane();
        tipLabel = new Label("图层");
    }

    @Override
    public void initBar() {
        tipLabel.setTextFill(Color.web("#0076a3"));
        tipLabel.setFont(new Font("Microsoft YaHei", 16));

        vBox.getChildren().add(tipLabel);
        vBox.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    @Override
    public VBox getBar() {
        return vBox;
    }


    public void setLayers(Group group) {
        Platform.runLater(() -> {
            boradPaneGroup = group;
            contentVBox.getChildren().clear();
            scrollPane.setContent(null);

            for (int i = 1; i < boradPaneGroup.getChildren().size(); i++) {
                Node shape = boradPaneGroup.getChildren().get(i);
                if(shape instanceof MyPen || shape instanceof TextArea){
                    continue;
                }

                ShapeButton button = new ShapeButton(shape);
                ShapeLabel shapeLabel = new ShapeLabel(shape.toString());
                lableEvent(shape, shapeLabel ,button);

                String toolTipString = dealShapeString(shape.toString());
                Tooltip tooltip = new Tooltip(toolTipString);
                ToolTipUtil.setTipTime(tooltip, 5000);
                shapeLabel.setTooltip(tooltip);

                HBox hBox = new HBox();
                hBox.getChildren().add(button);
                hBox.getChildren().add(shapeLabel);
                contentVBox.getChildren().add(hBox);
            }

            scrollPane.setMaxWidth(300);
            scrollPane.setPrefHeight(150);
            scrollPane.setContent(contentVBox);
        });
    }

    DropShadow shadow = new DropShadow();

    private String dealShapeString(String shapeString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < shapeString.length(); i++) {
            if (i % 30 == 0 && i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(shapeString.charAt(i));
        }
        return stringBuilder.toString();
    }

    private void lableEvent(Node shape, Label label, ShapeButton button) {
        label.setOnMouseClicked(event -> {
            label.setEffect(null);
            if (event.getButton() == MouseButton.SECONDARY) {
                label.setEffect(shadow);
                label.setOnContextMenuRequested(e -> {
                    layersMenuPane.show(scrollPane, event.getScreenX(), event.getScreenY());
                    layersMenuPane.setShape(shape,button);
                    label.setEffect(null);
                });
            }
        });
    }
}

class ShapeButton extends Button {

    private final String iconShowPath = PngPathEnum.SHOW.toString();
    private final String iconHidePath = PngPathEnum.HIDE.toString();

    private ImageView showImg = GetImageView.getImageView(new Image(iconShowPath));
    private ImageView hideImg = GetImageView.getImageView(new Image(iconHidePath));

    private Node shape;

    private boolean isShow = true;

    public ShapeButton(Node shape) {
        this.shape = shape;
        setStyle("-fx-background-color: transparent;");
        showImg.setFitWidth(15);
        showImg.setFitHeight(15);
        hideImg.setFitWidth(15);
        hideImg.setFitHeight(15);
        isShow = true;
        setGraphic(showImg);
        buttonEvent();
    }

    public void setShowImg() {
        setGraphic(showImg);
        isShow = true;
    }

    public void setHideImg() {
        setGraphic(hideImg);
        isShow = false;
    }

    private void buttonEvent() {
        this.setOnMouseClicked(event -> {
            if (isShow) {
                shape.setVisible(false);
                setGraphic(hideImg);
                isShow = false;
            } else {
                shape.setVisible(true);
                setGraphic(showImg);
                isShow = true;
            }
        });
    }
}

class ShapeLabel extends Label {

    private String name;
    private String iconName;
    private String iconPath;

    public ShapeLabel(String shapeName) {
        String[] spiltShape = shapeName.split("\\[");
        name = spiltShape[0];
        String[] spiltName = name.split("@");
        iconName = spiltName[0].toUpperCase().substring(2);
        initPath();
    }

    public void initPath() {
        if (iconName.equals("STRAITLINE")) {
            iconPath = PngPathEnum.LINE.toString();
        }  else if (iconName.equals("MOUSE")) {
            iconPath = PngPathEnum.MOUSE.toString();
        } else if (iconName.equals("TEXT")) {
            iconPath = PngPathEnum.TEXT.toString();
        }  else if (iconName.equals("RECTANGLE")) {
            iconPath = PngPathEnum.RECTANGLEZ.toString();
        } else if (iconName.equals("RECTANGLEY")) {
            iconPath = PngPathEnum.RECTANGLEY.toString();
        } else if (iconName.equals("ELLIPSE")) {
            iconPath = PngPathEnum.OVAL.toString();
        } else if (iconName.equals("CIRCLE")) {
            iconPath = PngPathEnum.OVAL.toString();
        } else if (iconName.equals("POLYGON")) {
            iconPath = PngPathEnum.POLYGON.toString();
        }
        ImageView imageView = GetImageView.getImageView(new Image(iconPath));
        if (iconName.equals("STRAITLINE")) {
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
        }
        setGraphic(imageView);
        setContentDisplay(ContentDisplay.LEFT);
        setText(name);
    }
}

/**
 * 右键菜单类
 */
class LayersMenuPane extends ContextMenu {
    private static LayersMenuPane instance = null;
    private Node shape;
    private ShapeButton button;

    public void setShape(Node shape,ShapeButton button) {
        this.shape = shape;
        this.button = button;
    }

    private LayersMenuPane() {
        MenuItem showBtn = new MenuItem("隐藏");
        showBtn.setOnAction(e -> {
            shape.setVisible(false);
            button.setHideImg();
        });

        MenuItem hideBtn = new MenuItem("显示");
        hideBtn.setOnAction(e -> {
            shape.setVisible(true);
            button.setShowImg();
        });
        getItems().add(showBtn);
        getItems().add(hideBtn);
    }

    public static LayersMenuPane getInstance() {
        if (instance == null) {
            instance = new LayersMenuPane();
        }
        return instance;
    }
}
