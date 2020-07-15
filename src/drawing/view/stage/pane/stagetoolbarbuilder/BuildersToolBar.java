package drawing.view.stage.pane.stagetoolbarbuilder;

import drawing.config.PngPathEnum;
import drawing.config.ShapeProperty;
import drawing.model.StageToolButton;
import drawing.utils.GetImageView;
import drawing.utils.ToolTipUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class BuildersToolBar implements Builders {

    public void setDetailBar(BuildersDetailBar detailBar) {
        this.detailBar = detailBar;
    }

    public void setTextBar(BuildersTextBar textBar) {
        this.textBar = textBar;
    }

    private VBox vBox;
    private BuildersDetailBar detailBar;
    private BuildersTextBar textBar;
    private TilePane tilePane;
    private List<StageToolButton> toolButton = new ArrayList<>();
    private Tooltip tooltip1 = new Tooltip("画笔");
    private Tooltip tooltip2 = new Tooltip("橡皮擦");
    private Tooltip tooltip3 = new Tooltip("鼠标");
    private Tooltip tooltip4 = new Tooltip("文本输入");
    private Tooltip tooltip5 = new Tooltip("直线");
    private Tooltip tooltip6 = new Tooltip("正方形，按Shift创建矩形");
    private Tooltip tooltip7 = new Tooltip("圆角正方形，按Shift创建圆角矩形");
    private Tooltip tooltip8 = new Tooltip("圆形，按Shift椭圆");
    private Tooltip tooltip9 = new Tooltip("多边形");
    private Tooltip tooltip10 = new Tooltip("自定义绘制\n多边形");

    @Override
    public void createBar() {
        vBox = new VBox();
        tilePane = new TilePane();
    }

    @Override
    public void initBar() {
        tilePane.setAlignment(Pos.CENTER_LEFT);
        tilePane.setStyle("-fx-background-color: #f0f2f3");
        tilePane.setPrefColumns(5);
        tilePane.setHgap(8);
        tilePane.setVgap(12);


        ToolTipUtil.setTipTime(tooltip1, 5000);
        ToolTipUtil.setTipTime(tooltip2, 5000);
        ToolTipUtil.setTipTime(tooltip3, 5000);
        ToolTipUtil.setTipTime(tooltip4, 5000);
        ToolTipUtil.setTipTime(tooltip5, 5000);
        ToolTipUtil.setTipTime(tooltip6, 5000);
        ToolTipUtil.setTipTime(tooltip7, 5000);
        ToolTipUtil.setTipTime(tooltip8, 5000);
        ToolTipUtil.setTipTime(tooltip9, 5000);
        ToolTipUtil.setTipTime(tooltip10, 5000);

        Image ImgBarrel = new Image(PngPathEnum.MOUSE.toString());
        StageToolButton barrelButton = new StageToolButton("MOUSE");
        barrelButton.setGraphic(GetImageView.getImageView(ImgBarrel));
        barrelButton.setTooltip(tooltip3);
        toolButton.add(barrelButton);


        Image ImgPen = new Image(PngPathEnum.PEN.toString(), false);
        StageToolButton penButton = new StageToolButton("PEN");
        penButton.setGraphic(GetImageView.getImageView(ImgPen));
        penButton.setTooltip(tooltip1);
        toolButton.add(penButton);

        Image ImgRubber = new Image(PngPathEnum.RUBBER.toString());
        StageToolButton rubberButton = new StageToolButton("RUBBER");
        rubberButton.setGraphic(GetImageView.getImageView(ImgRubber));
        rubberButton.setTooltip(tooltip2);
        toolButton.add(rubberButton);


        Image ImgText = new Image(PngPathEnum.TEXT.toString());
        StageToolButton textButton = new StageToolButton("TEXT");
        textButton.setGraphic(GetImageView.getImageView(ImgText));
        textButton.setTooltip(tooltip4);
        toolButton.add(textButton);

        Image ImgLine = new Image(PngPathEnum.LINE.toString());
        StageToolButton lineButton = new StageToolButton("LINE");
        lineButton.setGraphic(GetImageView.getImageView(ImgLine));
        lineButton.setTooltip(tooltip5);
        toolButton.add(lineButton);

        Image ImgSquare = new Image(PngPathEnum.RECTANGLEZ.toString());
        StageToolButton squareButton = new StageToolButton("SQUARE");
        squareButton.setGraphic(GetImageView.getImageView(ImgSquare));
        squareButton.setTooltip(tooltip6);
        toolButton.add(squareButton);

        Image ImgSquareY = new Image(PngPathEnum.RECTANGLEY.toString());
        StageToolButton squareYButton = new StageToolButton("SQUAREY");
        squareYButton.setGraphic(GetImageView.getImageView(ImgSquareY));
        squareYButton.setTooltip(tooltip7);
        toolButton.add(squareYButton);

        Image ImgOval = new Image(PngPathEnum.OVAL.toString());
        StageToolButton ovalButton = new StageToolButton("OVAL");
        ovalButton.setGraphic(GetImageView.getImageView(ImgOval));
        ovalButton.setTooltip(tooltip8);
        toolButton.add(ovalButton);

        Image ImgPolygon = new Image(PngPathEnum.POLYGON.toString());
        StageToolButton polygonButton = new StageToolButton("POLYGON");
        polygonButton.setGraphic(GetImageView.getImageView(ImgPolygon));
        polygonButton.setTooltip(tooltip9);
        toolButton.add(polygonButton);

        Image ImgPolygon1 = new Image(PngPathEnum.POLYGON1.toString());
        StageToolButton polygonButton1 = new StageToolButton("POLYGON1");
        polygonButton1.setTooltip(tooltip10);
        polygonButton1.setGraphic(GetImageView.getImageView(ImgPolygon1));
        toolButton.add(polygonButton1);

        DropShadow shadow = new DropShadow();
        for (Button curBt : toolButton) {
            curBt.setStyle("-fx-base: #f0f2f3;-fx-background-insets: 0;");
            curBt.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                curBt.setEffect(shadow);
            });

            curBt.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                curBt.setEffect(null);
            });

            curBt.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                String name = ((StageToolButton) e.getSource()).getName();
                ShapeProperty.setToolName(name);
                if (name == "TEXT") {
                    textBar.getLabel().setText("文本框");
                    detailBar.setFont();
                } else if (name == "LINE") {
                    if (name == "LINE") {
                        textBar.getLabel().setText("直 线");
                    }
                    detailBar.setSize(0);
                } else if (name == "RUBBER" || name == "PEN") {
                    if (name == "RUBBER") {
                        textBar.getLabel().setText("橡皮擦");
                    } else if (name == "PEN") {
                        textBar.getLabel().setText("画 笔");
                    }
                    detailBar.setSize(-1);
                } else if (name == "MOUSE") {
                    textBar.getLabel().setText("鼠标");
                    detailBar.setSize(2);
                } else if (name == "OVAL") {
                    textBar.getLabel().setText("圆形");
                    detailBar.setSize(1);
                } else if (name == "SQUARE") {
                    textBar.getLabel().setText("正方形");
                    detailBar.setSize(1);
                } else if (name == "SQUAREY") {
                    textBar.getLabel().setText("正方形(圆角)");
                    detailBar.setSize(1);
                } else if (name == "POLYGON") {
                    textBar.getLabel().setText("多边形");
                    detailBar.setSize(3);
                } else if (name == "POLYGON1") {
                    textBar.getLabel().setText("多边形(手绘)");
                    detailBar.setSize(4);
                } else {
                    detailBar.setSize(1);
                }
                // if (name == "TEXT") {
                //     TextInputDialog dialog = new TextInputDialog("");
                //     dialog.setTitle("文本输入框");
                //     dialog.setContentText("请输入");
                //     dialog.setHeaderText("修改字体后，直接在画布点击");
                //     Optional<String> result = dialog.showAndWait();
                //     if (result.isPresent()) {
                //         ShapeProperty.setText(result.get());
                //     }
                // }
            });
        }
        tilePane.getChildren().addAll(toolButton);
        vBox.getChildren().add(tilePane);
    }

    @Override
    public VBox getBar() {
        return vBox;
    }
}
