package drawing.view.stage.pane;

import drawing.config.PngPathEnum;
import drawing.config.SizeEnum;
import drawing.model.StageToolButton;
import drawing.utils.FileSaveWithPng;
import drawing.utils.FileUtil;
import drawing.utils.GetImageView;
import drawing.view.MainStage;
import drawing.view.stage.alert.AlertBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

public class MenuBarPane implements StageBasePane {

    private VBox vBox;
    private MainStage mainStage;
    private MenuBar menubar;
    private Menu fileMenu;
    private Menu editMenu;
    private Menu helpMenu;
    private MenuItem menuOpen, menuSave, menuSaveAs, menuClose, menuNew;
    private MenuItem menuUndo, menuRedo, menuCopy, menuPaste, menuDelete, menuClear;
    private MenuItem menuAbout;

    private Menu up, down, left, right;

    @Override
    public void createPane(MainStage mainStage) {
        this.mainStage = mainStage;
        vBox = new VBox();
        initPane();
    }

    @Override
    public void initPane() {
        menubar = new MenuBar();
        initFileMenu();
        initEdieMenu();
        initHelpMenu();
        initBtn();

        menubar.getMenus().add(fileMenu);
        menubar.getMenus().add(editMenu);
        menubar.getMenus().add(helpMenu);
        //menubar.getMenus().add(up);
        vBox.getChildren().add(menubar);
    }

    @Override
    public VBox getPane() {
        return vBox;
    }

    private void createNew() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        ButtonType customizeBtn = new ButtonType("自定义画板", ButtonBar.ButtonData.OK_DONE);
        ButtonType defaultBtn = new ButtonType("默认画板", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(customizeBtn, defaultBtn);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField width = new TextField();
        width.setPromptText("画板宽度");
        TextField height = new TextField();
        height.setPromptText("画板长度");

        grid.add(new Label("画板宽:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("画板长:"), 0, 1);
        grid.add(height, 1, 1);

        Node customButton = dialog.getDialogPane().lookupButton(customizeBtn);
        customButton.setDisable(true);

        width.textProperty().addListener((observable, oldValue, newValue) -> {
            customButton.setDisable(newValue.trim().isEmpty());
        });


        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> width.requestFocus());
        //Platform.runLater(() -> height.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == customizeBtn) {
                return new Pair<>(width.getText(), height.getText());
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

    private void initBtn() {
        up = new Menu();
        ImageView imageView =GetImageView.getImageView(new Image(PngPathEnum.UP.toString()));
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        up.setGraphic(imageView);

    }

    private void initFileMenu() {

        fileMenu = new Menu();
        fileMenu.setText("菜单");
        fileMenu.setStyle("-fx-font-size:16;");

        {
            menuNew = new MenuItem("新建");
            menuNew.setStyle("-fx-font-size:16;");
            menuNew.setAccelerator((new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN)));
            menuNew.setOnAction((ActionEvent t) -> {
                //添加新建画布
                mainStage.getRootLayout().getCenter().setVisible(true);
                mainStage.getRootLayout().getRight().setDisable(false);
                mainStage.getBoard().clear();

            });
        }

        {
            menuSave = new MenuItem();
            menuSave.setText("保存");
            menuSave.setStyle("-fx-font-size:16;");
            menuSave.setAccelerator(KeyCombination.keyCombination("Ctrl+s"));
            menuSave.setOnAction((ActionEvent t) -> {
                FileUtil.saveShape(mainStage, mainStage.getBoard().getSaveShapes());
            });
        }

        {
            menuSaveAs = new MenuItem();
            menuSaveAs.setText("保存为PNG");
            menuSaveAs.setStyle("-fx-font-size:16;");
            menuSaveAs.setAccelerator((new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN)));
            menuSaveAs.setOnAction((ActionEvent t) -> {
                //FileSaveWithPng.saveToFile(mainStage);
                FileSaveWithPng.saveBySnapshot(mainStage);
            });
        }

        {
            menuOpen = new MenuItem();
            menuOpen.setText("打开");
            menuOpen.setStyle("-fx-font-size:16;");
            menuOpen.setOnAction((ActionEvent t) -> {
                FileUtil.openShape(mainStage);
            });
        }


        {
            menuClose = new MenuItem();
            menuClose.setText("退出");
            //menuClose.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
            menuClose.setStyle("-fx-font-size:16;");
            menuClose.setOnAction((ActionEvent t) -> {
                if (mainStage.getBoard().getSaveShapes().size() == 0) {
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
                        FileUtil.saveShape(mainStage, mainStage.getBoard().getSaveShapes());
                    }  else if (result.get() == buttonTypeExit) {
                        Platform.exit();
                    }
                }
            });
        }

        fileMenu.getItems().add(menuNew);
        fileMenu.getItems().add(menuOpen);
        fileMenu.getItems().add(menuSave);
        fileMenu.getItems().add(menuSaveAs);
        fileMenu.getItems().add(menuClose);
    }

    private void initEdieMenu() {
        editMenu = new Menu();
        editMenu.setText("编辑");
        editMenu.setStyle("-fx-font-size:16;");

        {
            menuUndo = new MenuItem("撤销");
            menuUndo.setStyle("-fx-font-size:16;");
            menuUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN));
            menuUndo.setOnAction((ActionEvent t) -> {
                //撤销操作
                mainStage.getBoard().undo();
            });
        }
        {
            menuRedo = new MenuItem("重做");
            menuRedo.setStyle("-fx-font-size:16;");
            menuRedo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            menuRedo.setOnAction((ActionEvent t) -> {
                //撤销操作
                mainStage.getBoard().redo();
            });
        }
        {
            menuCopy = new MenuItem("复制");
            menuCopy.setStyle("-fx-font-size:16;");
            menuCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
            menuCopy.setOnAction((ActionEvent t) -> {
                //撤销操作
                mainStage.getBoard().copyChosedShapes();
            });
        }
        {
            menuPaste = new MenuItem("粘贴");
            menuPaste.setStyle("-fx-font-size:16;");
            menuPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN));
            menuPaste.setOnAction((ActionEvent t) -> {
                //撤销操作
                mainStage.getBoard().pasteChosedShapes();
            });
        }
        {
            menuDelete = new MenuItem("删除");
            menuDelete.setStyle("-fx-font-size:16;");
            menuDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
            menuDelete.setOnAction((ActionEvent t) -> {
                //撤销操作
                mainStage.getBoard().deleteChosedShapes();
            });
        }
        {
            menuClear = new MenuItem("重置画布");
            menuClear.setStyle("-fx-font-size:16;");
            menuClear.setAccelerator((new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN)));
            menuClear.setOnAction((ActionEvent t) -> {
                //添加新建画布
                mainStage.getBoard().clear();
            });
        }

        editMenu.getItems().add(menuUndo);
        editMenu.getItems().add(menuRedo);
        editMenu.getItems().add(menuCopy);
        editMenu.getItems().add(menuPaste);
        editMenu.getItems().add(menuDelete);
        editMenu.getItems().add(menuClear);
    }

    private void initHelpMenu() {
        helpMenu = new Menu();
        helpMenu.setText("帮助");
        helpMenu.setStyle("-fx-font-size:16;");
        menuAbout = new MenuItem();
        menuAbout.setText("关于");
        menuAbout.setStyle("-fx-font-size:16;");
        menuAbout.setOnAction((ActionEvent t) -> {
            new AlertBuilder.builder(new Alert(Alert.AlertType.INFORMATION))
                    .setTitle("关于")
                    .setHeaderText("Base by JavaFx\n")
                    .setStageStyle(StageStyle.DECORATED)
                    .setContentText("版本：1.0.1\n" +
                            "鸣谢媛圈圈提供的部分icon")
                    .setStage()
                    .setIconPath(PngPathEnum.ABOUT.toString())
                    .build();
        });
        helpMenu.getItems().add(menuAbout);
    }

}
