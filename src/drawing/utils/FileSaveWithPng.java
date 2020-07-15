package drawing.utils;

import drawing.config.PngPathEnum;
import drawing.model.MyCircle;
import drawing.model.MyEllipse;
import drawing.model.MyPen;
import drawing.model.MyShape;
import drawing.view.MainStage;
import drawing.view.stage.alert.AlertBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileSaveWithPng {
    private static Canvas canvas;
    private static int canvasWidth;
    private static int canvasHeigth;
    private static MainStage stage;

    private static ArrayList<Double> lineToList = new ArrayList<>();

    /**
     * 生成文件选择器，调用生成器将图层转化为image写入文件
     */
    public static void saveToFile(MainStage stage1) {
        stage = stage1;
        canvasHeigth = stage.getBoard().getDrawingCanvasHeight();
        canvasWidth = stage.getBoard().getDrawingCanvasWidth();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setTitle("保存图片");
        File img = fc.showSaveDialog(null);
        String type = "PNG";

        try {
            Canvas myCanvas = createCanvas(stage.getBoard().getGroup());
            WritableImage writableImage = new WritableImage(canvasWidth, canvasHeigth);
            myCanvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            if (img != null) {
                ImageIO.write(renderedImage, type, img);
            }

            new AlertBuilder.builder(new Alert(Alert.AlertType.INFORMATION))
                    .setTitle("保存文件")
                    .setHeaderText(null)
                    .setStageStyle(StageStyle.DECORATED)
                    .setContentText("成功保存文件！\n" + "保存路径为: " + img)
                    .setStage()
                    .setIconPath(PngPathEnum.ABOUT.toString())
                    .build();
            img = null;
            myCanvas = null;
        } catch (IOException ignored) {
        }
    }

    /**
     * 画布生成器，将所有图层合并成一个canvas
     *
     * @param group 传入所有图层
     * @return canvas
     */
    private static Canvas createCanvas(Group group) {
        Canvas tempCanvas = new Canvas(canvasWidth, canvasHeigth);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        for (int i = 1; i < group.getChildren().size(); i++) {
            MyShape shape = (MyShape) group.getChildren().get(i);
            if(shape instanceof MyPen) {
                lineToList.add(shape.getPosX());
                lineToList.add(shape.getPosY());
                continue;
            }
        }

        for (int i = 1; i < group.getChildren().size(); i++) {
            MyShape shape = (MyShape) group.getChildren().get(i);
            double radiusX = 0;
            double radiusY = 0;
            if(shape instanceof MyCircle){
                radiusY = ((MyCircle) shape).getMyRadius();
                radiusX = radiusY;
            } else if(shape instanceof MyEllipse) {
                radiusX = ((MyEllipse) shape).getMyRadiusX();
                radiusY = ((MyEllipse) shape).getMyRadiusY();
            } else if(shape instanceof MyPen) {

            }
            System.out.println(shape);
            WritableImage image = group.getChildren().get(i).snapshot(params, null);
            tempCanvas.getGraphicsContext2D().drawImage(image, shape.getPosX()-radiusX, shape.getPosY()-radiusY);
        }
        return tempCanvas;
    }

    public static void saveBySnapshot(MainStage stage1) {
        stage = stage1;
        canvasHeigth = stage.getBoard().getDrawingCanvasHeight();
        canvasWidth = stage.getBoard().getDrawingCanvasWidth();
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
            fc.setTitle("保存图片为PNG");
            File img = fc.showSaveDialog(null);
            String type = "PNG";

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            Group myCanvas = stage.getBoard().getGroup();
            WritableImage writableImage = new WritableImage(canvasWidth, canvasHeigth);
            myCanvas.snapshot(params, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            if (img != null) {
                ImageIO.write(renderedImage, type, img);
            }

            new AlertBuilder.builder(new Alert(Alert.AlertType.INFORMATION))
                    .setTitle("保存文件")
                    .setHeaderText(null)
                    .setStageStyle(StageStyle.DECORATED)
                    .setContentText("成功保存文件！\n" + "保存路径为: " + img)
                    .setStage()
                    .setIconPath(PngPathEnum.ABOUT.toString())
                    .build();
        } catch (IOException ignored) {

        }
    }
}
