package drawing.utils;


import drawing.config.PngPathEnum;
import drawing.model.MyShape;
import drawing.view.MainStage;
import drawing.view.stage.alert.AlertBuilder;
import javafx.scene.control.Alert;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件保存打开类
 */
public final class FileUtil {

    public static void openShape(MainStage mainStage) {
        //打开
        FileChooser filePicker = new FileChooser();
        filePicker.setTitle("读取形状");
        filePicker.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape", "*.s", "*.shape", "*.SHAPE", "*.Shape"));
        File f = filePicker.showOpenDialog(mainStage);
        ArrayList<Shape> shapes = FileUtil.readFromFile(f.getPath());
        mainStage.getBoard().addShapes(shapes);
        System.out.println("read path:" + f.getPath());
    }

    public static void saveShape(Stage stage, ArrayList<Shape> objList) {
        if (objList.size() == 0) {
            return;
        }
        FileChooser filePicker = new FileChooser();
        filePicker.setTitle("保存形状");
        filePicker.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.shape", "*.shape", "*.SHAPE", "*.Shape"));

        File savePath = filePicker.showSaveDialog(stage);
        if(savePath == null) {
            return;
        }
        FileUtil.writeObjectToFile(objList, savePath.getPath());
    }

    public static ArrayList<Shape> readFromFile(String filePath) {
        System.out.println("readShape...");
        ArrayList<Shape> objList = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(filePath);
             ObjectInputStream objReader = new ObjectInputStream(inputStream)) {
            MyShape obj;
            while (true) {
                try {
                    obj = (MyShape) objReader.readObject();
                    try {
                        objList.add(obj.reply());
                    } catch (ClassCastException e) {
                        System.out.println(e.getMessage());
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("readShape done...");
        }

        return objList;
    }

    public static void writeObjectToFile(ArrayList<Shape> objList, String filePath) {
        try (FileOutputStream outfile = new FileOutputStream(filePath);
             ObjectOutputStream objWriter = new ObjectOutputStream(outfile)) {
            for (int i = 0; i < objList.size(); i++) {
                System.out.println("writing to file:" + objList.get(i));
                objWriter.writeObject(objList.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write shape done...");
         new AlertBuilder.builder(new Alert(Alert.AlertType.INFORMATION))
                .setTitle("保存文件")
                .setHeaderText(null)
                .setStageStyle(StageStyle.DECORATED)
                .setContentText("成功保存文件！\n" + "保存路径为: " + filePath)
                .setStage()
                .setIconPath(PngPathEnum.ABOUT.toString())
                .build();
    }

}
