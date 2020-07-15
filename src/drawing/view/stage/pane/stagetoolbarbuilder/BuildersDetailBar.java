package drawing.view.stage.pane.stagetoolbarbuilder;

import drawing.config.ShapeProperty;
import drawing.config.SizeEnum;
import drawing.utils.FontUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class BuildersDetailBar implements Builders {
    private VBox vBox;
    private BuildersColorBar colorBar;
    /**
     * 设置宽度
     */
    private TextField penValue;
    /**
     * 设置多边形边数
     */
    private TextField polygonValue;
    private Label tipLabel;
    private Label polygonLabel;

    /**
     * 滑动选择
     */
    private Slider sliderPen;
    private CheckBox checkBox;
    private CheckBox dashLine;
    private CheckBox fontCB;
    private GridPane gridPane;

    /**
     * 字体大小
     */
    private ComboBox<Integer> fontSize;
    /**
     * 字体类型
     */
    private ComboBox<String> fontName;

    private ObservableList<Integer> fontSizeItems = FXCollections.observableArrayList();
    private ObservableList<String> fontNameItems = FXCollections.observableArrayList();

    @Override
    public void createBar() {
        vBox = new VBox();
        penValue = new TextField();
        polygonValue = new TextField();
        sliderPen = new Slider();
        tipLabel = new Label("  粗细");
        polygonLabel = new Label("              多边形边数");
    }

    @Override
    public void initBar() {
        vBox.setAlignment(Pos.CENTER);

        tipLabel.setTextFill(Color.web("#0076a3"));
        tipLabel.setFont(new Font("Microsoft YaHei", 16));

        polygonLabel.setTextFill(Color.web("#0076a3"));
        polygonLabel.setFont(new Font("Microsoft YaHei", 16));

        initPenPane(-1);
        initFont();
        vBox.getChildren().add(gridPane);
    }

    @Override
    public VBox getBar() {
        return vBox;
    }

    public void setColorBar(BuildersColorBar colorBar) {
        this.colorBar = colorBar;
    }

    private void initFont() {

        fontCB = new CheckBox("填充");
        fontCB.setSelected(true);
        fontCB.setPrefSize(80,40);
        fontCB.selectedProperty().addListener(new ChangeListener<Boolean>() { // 设置复选框的勾选监听器
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (arg2) {
                    ShapeProperty.setBorderSize("FILL");
                } else if (arg1) {
                    ShapeProperty.setBorderSize(String.valueOf(0.2));
                }
            }
        });

        // 初始化字号
        fontSize = new ComboBox<Integer>();
        fontSize.setStyle("-fx-base:#ffffff;-fx-background-color:#f0f2f3;");
        fontSize.setPrefWidth(SizeEnum.DETAIL_WIDTH.getValue());
        for (int i = 8; i <= 48; i += 2) {
            fontSizeItems.add(i);
        }
        fontSize.setItems(fontSizeItems);
        fontSize.getSelectionModel().select(0);
        fontSize.valueProperty().addListener((ov, oldv, newv) -> {
            ShapeProperty.setFontSize(Integer.valueOf(newv));
        });

        // 初始化字体
        fontName = new ComboBox<String>();
        ArrayList<String> fonts = FontUtil.getFontFamilies();
        for (int i = 0; i < fonts.size(); i++) {
            String fontFamily = fonts.get(i);
            fontNameItems.add(fontFamily);
        }
        fontName.setStyle("-fx-base:#f0f2f3;-fx-background-color:#ffffff;");
        fontName.setPrefWidth(SizeEnum.DETAIL_WIDTH.getValue());
        fontName.setItems(fontNameItems);
        fontName.getSelectionModel().select(0);
        fontName.valueProperty().addListener((ov, oldv, newv) -> {
            ShapeProperty.setFontName(newv);
        });
    }

    private void initPenPane(int type) {

        vBox.setPadding(new Insets(0, 5, 40, 10));

        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        checkBox = new CheckBox("填充");
        checkBox.setSelected(false);
        dashLine = new CheckBox("虚线");
        dashLine.setSelected(false);

        sliderPen.setMin(1);
        sliderPen.setMax(100);
        sliderPen.setValue(3);
        sliderPen.setShowTickLabels(true);
        sliderPen.setPrefWidth(300);

        penValue.setText(Double.toString(sliderPen.getValue()));
        penValue.setPrefWidth(65);
        penValue.setAlignment(Pos.CENTER);

        polygonValue.setPrefWidth(55);
        polygonValue.setAlignment(Pos.CENTER);
        polygonValue.setText("3");

        sliderPen.setDisable(false);
        penValue.setDisable(false);
        dashLine.setDisable(true);
        if (type == -1 || type == 0) {
            if (type == -1) {
                dashLine.setVisible(true);
            } else {
                dashLine.setVisible(true);
                dashLine.setDisable(false);
            }
            sliderPen.setMax(40);
            tipLabel.setPrefWidth(160);
            dashLine.setPrefWidth(60);
            gridPane.add(tipLabel, 0, 0);
            gridPane.add(dashLine, 1, 0);
            gridPane.add(penValue, 2, 0);
            gridPane.add(sliderPen, 0, 1, 3, 1);
        } else if (type == 1) {
            sliderPen.setMax(40);
            tipLabel.setPrefWidth(90);
            checkBox.setPrefWidth(60);
            dashLine.setPrefWidth(60);
            gridPane.add(tipLabel, 0, 0);
            gridPane.add(dashLine, 1, 0);
            gridPane.add(checkBox, 2, 0);
            gridPane.add(penValue, 3, 0);
            gridPane.add(sliderPen, 0, 1, 4, 1);

        } else if (type == 2) {
            tipLabel.setPrefWidth(225);
            sliderPen.setDisable(true);
            penValue.setDisable(true);
            gridPane.add(tipLabel, 0, 0);
            gridPane.add(penValue, 1, 0);
            gridPane.add(sliderPen, 0, 1, 2, 1);

        } else if (type == 3 || type == 4) {
            sliderPen.setMax(40);
            tipLabel.setPrefWidth(90);
            checkBox.setPrefWidth(60);
            dashLine.setPrefWidth(60);
            polygonValue.setDisable(false);
            if (type == 4) {
                polygonValue.setDisable(true);
            }
            gridPane.add(tipLabel, 0, 0);
            gridPane.add(dashLine, 1, 0);
            gridPane.add(checkBox, 2, 0);
            gridPane.add(penValue, 3, 0);
            gridPane.add(sliderPen, 0, 1, 4, 1);
            gridPane.add(polygonLabel, 0, 2, 2, 1);
            gridPane.add(polygonValue, 2, 2);
            vBox.setPadding(new Insets(0, 5, 5, 10));
        }

        handlePenSlider(type);
    }

    /**
     * 处理滑动选择事件
     *
     * @param type
     */
    private void handlePenSlider(int type) {
        sliderPen.valueProperty().addListener((
                ObservableValue<? extends Number> ov, Number old_val,
                Number new_val) -> {
            penValue.setText(String.format("%.2f", new_val));
            if (type == 1 || type == 3 || type == 4) {
                ShapeProperty.setBorderSize(new_val.intValue() + "");
            } else if (type == 0 || type == -1) {
                ShapeProperty.setLineSize(new_val.intValue());
            }
        });

        // 设置复选框的勾选监听器
        checkBox.selectedProperty().addListener((arg0, arg1, arg2) -> {
            if (arg2) {
                sliderPen.setDisable(true);
                penValue.setDisable(true);
                dashLine.setDisable(true);
                ShapeProperty.setBorderSize("FILL");
            } else if (arg1) {
                sliderPen.setDisable(false);
                penValue.setDisable(false);
                dashLine.setDisable(false);
                ShapeProperty.setBorderSize(penValue.getText());
            }
        });

        dashLine.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sliderPen.setMax(5);
                checkBox.setDisable(true);
                ShapeProperty.setIsDashLine(true);
            } else if (oldValue) {
                checkBox.setDisable(false);
                ShapeProperty.setIsDashLine(false);
            }
        });

        penValue.setOnAction(event -> {
            double value = Double.parseDouble(penValue.getText());
            if (value > sliderPen.getMax()) value = sliderPen.getMax();
            if (value < sliderPen.getMin()) value = 1;
            sliderPen.setValue(value);
        });

        polygonValue.setOnAction(event -> {
            int value = Integer.parseInt(polygonValue.getText());
            if (value < 3) value = 3;
            ShapeProperty.polygonN = value;
        });
        polygonValue.textProperty().addListener((observable, oldValue, newValue) -> {
            int value = Integer.parseInt(polygonValue.getText());
            if (value < 3) value = 3;
            ShapeProperty.polygonN = value;
        });

    }

    /**
     * 输入字体初始化
     */
    public void setFont() {
        vBox.getChildren().clear();
        System.out.println(vBox.getChildren());
        fontCB.setSelected(true);
        fontName.getSelectionModel().select(0);
        fontSize.getSelectionModel().select(12);
        ShapeProperty.setBorderSize("FILL");
        ShapeProperty.setFontName("Arial");
        ShapeProperty.setFontSize(32);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().add(fontSize);
        hBox.getChildren().add(fontName);
        hBox.getChildren().add(fontCB);
        //vBox.getChildren().add(gridPane);
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(0, 5, 84, 10));
    }

    public void setSize(int type) {
        vBox.getChildren().clear();
        ShapeProperty.setIsDashLine(false);
        ShapeProperty.setBorderSize("3");
        ShapeProperty.setPolygonN(3);
        ShapeProperty.setColor(Color.BLACK);
        ShapeProperty.setBorderSize(penValue.getText());
        colorBar.resetColor();
        initPenPane(type);
        vBox.getChildren().add(gridPane);
    }

}
