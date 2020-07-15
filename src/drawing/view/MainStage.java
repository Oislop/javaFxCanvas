package drawing.view;

import drawing.config.PngPathEnum;
import drawing.config.SizeEnum;
import drawing.utils.FileUtil;
import drawing.utils.ScreenUtil;
import drawing.view.stage.*;
import drawing.view.stage.pane.BoardPane;
import drawing.view.stage.pane.MenuBarPane;
import drawing.view.stage.pane.StatusBarPane;
import drawing.view.stage.pane.ToolBarPane;
import drawing.view.stage.alert.AlertBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;


public class MainStage extends Stage {
    private BorderPane rootLayout;
    /**
     * 主绘制Pane
     */
    private BoardPane boardPane;
    /**
     * 鼠标信息Pane
     */
    private StatusBarPane statusBarPane;
    /**
     * 工具Pane
     */
    private ToolBarPane toolBarPane;
    /**
     * 菜单Pane
     */
    private MenuBarPane menuBarPane;

    private Scene scene;

    private VBox menuBarVBox;
    private VBox toolBarVBox;
    private VBox statusBarVBox;
    private VBox BoardVBox;

    /**
     * 静态实例
     */
    private static MainStage instance = null;

    /**
     * 私有化构造器
     */
    private MainStage() {

        rootLayout = new BorderPane();
        scene = new Scene(rootLayout);

        ScreenUtil.setScreen();

        rootLayout.setPrefSize(SizeEnum.STAGE_WIDTH.getValue(), SizeEnum.STAGE_HEIGHT.getValue());

        // 静态工厂，根据需求创建
        StageFactory stageFactory = new StageFactory();
        menuBarPane = (MenuBarPane) stageFactory.createPane("MenuBar");
        toolBarPane = (ToolBarPane) stageFactory.createPane("ToolBar");
        statusBarPane = (StatusBarPane) stageFactory.createPane("StatusBar");
        boardPane = (BoardPane) stageFactory.createPane("BoardPane");

        menuBarPane.createPane(this);
        toolBarPane.createPane(this);
        statusBarPane.createPane(this);
        boardPane.createPane(this);

        menuBarPane.getPane().prefWidthProperty().bind(this.widthProperty());
        rootLayout.setTop(menuBarPane.getPane());
        rootLayout.setCenter(boardPane.getPane());
        rootLayout.setRight(toolBarPane.getPane());
        rootLayout.setBottom(statusBarPane.getPane());

        // rootLayout.getCenter().setVisible(false);
        // rootLayout.getRight().setDisable(true);
        rootLayout.setOnMouseClicked(mouseCilckedEvt -> {
            if (!rootLayout.getCenter().isVisible()) {
                System.out.println("no");
                new AlertBuilder.builder(new Alert(Alert.AlertType.WARNING))
                        .setTitle("警告")
                        .setHeaderText(null)
                        .setStageStyle(StageStyle.DECORATED)
                        .setContentText("请先新建画板！")
                        .setStage()
                        .setIconPath(PngPathEnum.WARNING.toString())
                        .build();
            }
        });

        this.setOnCloseRequest(event -> {
            //Platform.exit();
            if (getBoard().getSaveShapes().size() == 0) {
                Platform.exit();
            } else {
                ButtonType buttonTypeExit = new ButtonType("直接退出", ButtonBar.ButtonData.NO);
                ButtonType buttonTypeSave = new ButtonType("保存文件", ButtonBar.ButtonData.OK_DONE);
                ButtonType buttonTypeCancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
                ArrayList<ButtonType> arrayList = new ArrayList<>();
                arrayList.add(buttonTypeExit);
                arrayList.add(buttonTypeSave);
                arrayList.add(buttonTypeCancel);
                AlertBuilder alertBuilder = new AlertBuilder.builder(new Alert(Alert.AlertType.CONFIRMATION))
                        .setTitle("退出")
                        .setHeaderText(null)
                        .setStageStyle(StageStyle.DECORATED)
                        .setContentText("文件未保存！是否继续退出")
                        .setStage()
                        .setIconPath(PngPathEnum.ABOUT.toString())
                        .setButtonTypes(arrayList)
                        .build();
                Optional<ButtonType> result = alertBuilder.getButtonTypeResult();
                if (result.get() == buttonTypeSave) {
                    FileUtil.saveShape(this, getBoard().getSaveShapes());
                } else if (result.get() == buttonTypeCancel) {
                    event.consume();
                } else if (result.get() == buttonTypeExit) {
                    Platform.exit();
                }
            }
        });


        setTitle("绘画板");
        getIcons().add(new Image(PngPathEnum.LOGO.toString()));
        setScene(scene);
        setResizable(true);
        show();
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public BoardPane getBoard() {
        return boardPane;
    }

    /**
     * 单例模式
     *
     * @return instance
     */
    public static MainStage getInstance() {
        if (instance == null)
            instance = new MainStage();
        return instance;
    }

}

@Deprecated
class DialogSize {
    public static void dialogSize() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        //dialog.initOwner(stage);
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        ButtonType customizeBtn = new ButtonType("自定义画板", ButtonBar.ButtonData.OK_DONE);
        ButtonType defaultBtn = new ButtonType("默认画板", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(customizeBtn, defaultBtn);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField width = new TextField("814");
        width.setPromptText("画板宽度");
        TextField height = new TextField("911");
        height.setPromptText("画板长度");

        grid.add(new Label("自定义画板宽:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("自定义画板长:"), 0, 1);
        grid.add(height, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> width.requestFocus());
        //Platform.runLater(() -> height.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == customizeBtn) {
                if (width.getText().trim().isEmpty() || height.getText().trim().isEmpty()) {
                    return new Pair<>("814", "911");
                }
                return new Pair<>(width.getText(), height.getText());
            }
            if (dialogButton == defaultBtn) {
                return new Pair<>("814", "911");
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(e -> {
            SizeEnum.CANVAS_HEIGHT.setValue(Integer.parseInt(e.getKey()));
            SizeEnum.CANVAS_WIDTH.setValue(Integer.parseInt(e.getValue()));
            System.out.println("Username=" + e.getKey() + ", Password=" + e.getValue());
        });

    }
}
