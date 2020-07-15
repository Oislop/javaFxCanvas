package drawing.view.stage.pane;

import drawing.config.PngPathEnum;
import drawing.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditPane {

    /**
     * VBox布局
     */
    private VBox vBox;

    /**
     * 设置旋转角度文本框
     */
    private TextField rotateValue;

    /**
     * 设置旋转角度
     */
    private Label rotateLabel;

    private ComboBox<Integer> rotateCB;
    private ObservableList<Integer> rotateCBItems = FXCollections.observableArrayList();


    /**
     * 旋转确认button
     */
    //private Button rotateBtn;

    /**
     * 滑动选择
     */
    private Slider sliderPen;
    /**
     * PenLabel
     */
    private Label penLabel;

    /**
     * penValue
     */
    private TextField penValue;
    /**
     * 是否填充
     */
    private CheckBox checkBox;
    /**
     * 是否虚线
     */
    private CheckBox dashLineBox;
    /**
     * 修改颜色
     */
    private Label colorTip;

    /**
     * 新修改颜色
     */
    private Label colorTipNew;
    /**
     * 原本颜色
     */
    private Label colorTipOld;

    /**
     * 颜色Label
     */
    private Label colorLabelOld;

    /**
     * 新颜色Label
     */
    private Label colorLabelNew;

    /**
     * 颜色选择器
     */
    private ColorPicker colorPicker;

    /**
     * 初始化颜色为黑色
     */
    private Color presentColor = Color.BLACK;

    /**
     * GridPane
     */
    private AnchorPane anchorPane;

    /**
     * Group
     */
    private Group root = new Group();

    /**
     * Scene
     */
    private Scene scene;

    /**
     * Stage
     */
    private Stage stage;

    /**
     * MyShape
     */
    private MyShape shape;

    /**
     * new shape
     */
    private Shape newObj = null;
    /**
     * 提交修改
     */
    private Button submit;

    /**
     * Shape 类型
     */
    private int type;
    /**
     * shapeBulider
     */
    private ShapeBuilder shapeBuilderNew;
    private ShapeBuilder shapeBuilder;
    private boolean dashLine = false;
    /**
     * 多选shape，一起修改
     */
    private ArrayList<Shape> allChosedShapes;

    /**
     * 是否填充
     */
    private boolean isFill = false;

    /**
     * 修改面板
     *
     * @param shape
     * @param stg
     */
    public EditPane(Shape shape, final Stage stg) {
        this.shape = (MyShape) shape;
        createPane(stg);
        System.out.println("edit shape: " + shape);
        // shape属性
        shapeConifg();
        // 构建builder
        setShapeBuilderNew();

        initPane();
        handlePane();

        root.getChildren().add(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void createPane(final Stage stg) {
        stage = new Stage();
        stage.initOwner(stg);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("编辑形状");
        stage.getIcons().add(new Image(PngPathEnum.EDIT.toString()));

        scene = new Scene(root, 480, 250, Color.valueOf("#f0f2f3"));

        vBox = new VBox();
        anchorPane = new AnchorPane();

        submit = new Button("修改");

        rotateValue = new TextField();
        rotateLabel = new Label("设置旋转角度");
        rotateCB = new ComboBox<>();
        rotateCB.setPrefWidth(80);
        rotateCB.setItems(rotateCBItems);
        rotateCB.getSelectionModel().select(0);
        for(int i=0; i<=360; i+=30){
            rotateCBItems.add(i);
        }

        penLabel = new Label("修改画笔大小");
        penValue = new TextField();
        sliderPen = new Slider();

        checkBox = new CheckBox("填充");
        checkBox.setSelected(false);

        dashLineBox = new CheckBox("虚线");
        dashLineBox.setSelected(false);

        colorTip = new Label("修改颜色");
        colorTipOld = new Label("修改前颜色");
        colorTipNew = new Label("修改后颜色");
        colorLabelNew = new Label();
        colorLabelOld = new Label();
        colorPicker = new ColorPicker();
    }


    private void initPane() {

        anchorPane.setPadding(new Insets(10, 10, 10, 10));

        //rotateBtn.setPrefWidth(60);
        rotateLabel.setPrefWidth(110);
        rotateValue.setPrefWidth(65);
        penValue.setPrefWidth(60);

        rotateLabel.setTextFill(Color.web("#0076a3"));
        rotateLabel.setFont(new Font("Microsoft YaHei", 16));
        penLabel.setTextFill(Color.web("#0076a3"));
        penLabel.setFont(new Font("Microsoft YaHei", 16));
        colorTip.setTextFill(Color.web("#0076a3"));
        colorTip.setFont(new Font("Microsoft YaHei", 16));
        colorTipOld.setTextFill(Color.web("#0076a3"));
        colorTipOld.setFont(new Font("Microsoft YaHei", 16));
        colorTipNew.setTextFill(Color.web("#0076a3"));
        colorTipNew.setFont(new Font("Microsoft YaHei", 16));

        sliderPen.setMin(1);
        sliderPen.setMax(40);
        sliderPen.setValue(3);
        sliderPen.setShowTickLabels(true);
        sliderPen.setPrefWidth(320);


        colorPicker.setPrefSize(150, 30);
        colorPicker.setValue(Color.BLACK);
        colorLabelOld.setPrefSize(60, 30);
        colorLabelNew.setPrefSize(60, 30);

        String color = "#" + colorPicker.getValue().toString().substring(2, 8);
        colorLabelOld.setStyle("-fx-background-color:" + color + ";");
        colorLabelNew.setStyle("-fx-background-color:" + color + ";");

        anchorPane.getChildren().addAll(penLabel, sliderPen, penValue,
                checkBox, dashLineBox,
                rotateLabel, rotateValue, rotateCB,
                colorTip, colorTipNew, colorTipOld,
                colorLabelNew, colorLabelOld, colorPicker,
                submit);
        AnchorPane.setTopAnchor(submit, 100.0);
        AnchorPane.setLeftAnchor(submit, 350.0);

        AnchorPane.setTopAnchor(penLabel, 20.0);
        AnchorPane.setLeftAnchor(penLabel, 20.0);

        AnchorPane.setTopAnchor(dashLineBox, 20.0);
        AnchorPane.setLeftAnchor(dashLineBox, 140.0);

        AnchorPane.setTopAnchor(checkBox, 20.0);
        AnchorPane.setLeftAnchor(checkBox, 200.0);

        AnchorPane.setTopAnchor(penValue, 20.0);
        AnchorPane.setLeftAnchor(penValue, 270.0);

        AnchorPane.setTopAnchor(sliderPen, 50.0);
        AnchorPane.setLeftAnchor(sliderPen, 20.0);

        AnchorPane.setTopAnchor(rotateLabel, 105.0);
        AnchorPane.setLeftAnchor(rotateLabel, 20.0);

        AnchorPane.setTopAnchor(rotateValue, 100.0);
        AnchorPane.setLeftAnchor(rotateValue, 170.0);

        AnchorPane.setTopAnchor(rotateCB, 100.0);
        AnchorPane.setLeftAnchor(rotateCB, 250.0);

        AnchorPane.setTopAnchor(colorTipOld, 160.0);
        AnchorPane.setLeftAnchor(colorTipOld, 20.0);

        AnchorPane.setTopAnchor(colorLabelOld, 155.0);
        AnchorPane.setLeftAnchor(colorLabelOld, 105.0);

        AnchorPane.setTopAnchor(colorTipNew, 160.0);
        AnchorPane.setLeftAnchor(colorTipNew, 185.0);

        AnchorPane.setTopAnchor(colorLabelNew, 155.0);
        AnchorPane.setLeftAnchor(colorLabelNew, 270.0);

        AnchorPane.setTopAnchor(colorTip, 205.0);
        AnchorPane.setLeftAnchor(colorTip, 20.0);

        AnchorPane.setTopAnchor(colorPicker, 200.0);
        AnchorPane.setLeftAnchor(colorPicker, 185.0);

    }

    /**
     * 事件处理
     *
     * @return
     */
    public Shape handlePane() {

        rotateCB.valueProperty().addListener((ov, oldv, newv) -> {
            rotateValue.setText(String.valueOf(newv));
        });

        penValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double value = Double.parseDouble(penValue.getText());
                if (value > sliderPen.getMax()) value = sliderPen.getMax();
                if (value < sliderPen.getMin()) value = 1;
                sliderPen.setValue(value);
            }
        });

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() { // 设置复选框的勾选监听器
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                presentColor = colorPicker.getValue();
                if (arg2) {
                    // FILL
                    sliderPen.setDisable(true);
                    penValue.setDisable(true);
                    dashLineBox.setDisable(true);
                    shapeBuilderNew.setFillColor(presentColor);
                    shapeBuilderNew.setLineColor(presentColor);
                    isFill = true;
                } else if (arg1) {
                    // NOT FILL
                    sliderPen.setDisable(false);
                    penValue.setDisable(false);
                    dashLineBox.setDisable(false);
                    shapeBuilderNew.setFillColor(null);
                    shapeBuilderNew.setLineColor(presentColor);
                    isFill = false;
                }
            }
        });

        dashLineBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean arg1, Boolean arg2) {
                if (arg2) {
                    // DASHLINE
                    sliderPen.setMax(5);
                    checkBox.setDisable(true);
                    shapeBuilderNew.setDashLine(true);
                } else if (arg1) {
                    // NOT DASHLINE
                    checkBox.setDisable(false);
                    shapeBuilderNew.setDashLine(false);
                }
            }
        });

        sliderPen.valueProperty().addListener((
                ObservableValue<? extends Number> ov, Number old_val,
                Number new_val) -> {
            penValue.setText(String.format("%.2f", new_val));
            shapeBuilderNew.setLineWidth(new_val.doubleValue());
        });


        colorPicker.setOnAction((ActionEvent t) -> {
            String color1 = "#" + colorPicker.getValue().toString().substring(2, 8);
            colorLabelNew.setStyle("-fx-background-color:" + color1 + ";");
            presentColor = colorPicker.getValue();
            if (isFill) {
                shapeBuilderNew.setFillColor(presentColor);
                shapeBuilderNew.setLineColor(presentColor);
            } else {
                shapeBuilderNew.setFillColor(null);
                shapeBuilderNew.setLineColor(presentColor);
            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reDraw();
                stage.hide();
            }
        });

        return newObj;
    }

    private void setShapeBuilderNew() {
        if (shapeBuilder.getFillColor() == null) {
            shapeBuilderNew = new ShapeBuilder.builder()
                    .setLineWidth(Double.parseDouble(penValue.getText()))
                    .setLineColor(presentColor.toString())
                    .setFillColor("")
                    .build();
        } else {
            shapeBuilderNew = new ShapeBuilder.builder()
                    .setLineWidth(Double.parseDouble(penValue.getText()))
                    .setLineColor(presentColor.toString())
                    .setFillColor("#" + shapeBuilder.getFillColor().toString().substring(2, 8))
                    .build();
        }
    }

    private void reDraw() {
        Double rorate = 0.0;
        if(!rotateValue.getText().equals("")){
            rorate = Double.parseDouble(rotateValue.getText());
        }
        dashLine = shapeBuilderNew.isDashLine();
        if (shape instanceof MyRectangle) {
            //画长方形
            MyRectangle myRectangle = new MyRectangle(
                    dashLine,
                    rorate,
                    ((MyRectangle) shape).isArc(),
                    shape.getPosX(),
                    shape.getPosY(),
                    ((MyRectangle) shape).getMyWidth(),
                    ((MyRectangle) shape).getMyHeight(),
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor());


            newObj = myRectangle;
        }
        if (shape instanceof MySquare) {
            //画正方形
            newObj = new MySquare(
                    dashLine,
                    rorate,
                    ((MyRectangle) shape).isArc(),
                    shape.getPosX(),
                    shape.getPosY(),
                    Math.max(((MySquare) shape).getMyWidth(), ((MySquare) shape).getMyHeight()),
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor());
        }

        if (shape instanceof MyCircle) {

            newObj = new MyCircle(
                    dashLine,
                    shape.getPosX(),
                    shape.getPosY(),
                    ((MyCircle) shape).getMyRadius(),
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor());

        }

        if (shape instanceof MyEllipse) {
            //画椭圆
            newObj = new MyEllipse(
                    dashLine,
                    rorate,
                    shape.getPosX(),
                    shape.getPosY(),
                    ((MyEllipse) shape).getMyRadiusX(),
                    ((MyEllipse) shape).getMyRadiusY(),
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor());
        }

        if (shape instanceof MyLine) {
            //画直线
            newObj = new MyLine(
                    dashLine,
                    shape.getPosX(),
                    shape.getPosY(),
                    shape.getPos()[2],
                    shape.getPos()[3],
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor());
        }

        if (shape instanceof MyPolygon) {
            //画多边形
            newObj = new MyPolygon(
                    dashLine,
                    rorate,
                    shapeBuilderNew.getLineWidth(),
                    shapeBuilderNew.getLineColor(),
                    shapeBuilderNew.getFillColor(),
                    shape.getPos());
        }
        //System.out.println(rotateValue.getText());
    }

    private void shapeConifg() {
        System.out.println(shape.isDashLine());
        shapeBuilder = new ShapeBuilder.builder()
                .setType(shape)
                .setLineWidth(shape.getLineWidth())
                .setLineColor(shape.getLineColor())
                .setFillColor(shape.getFillColor())
                .setDashLine(shape.isDashLine())
                .build();

        presentColor = shapeBuilder.getLineColor();
        colorPicker.setValue(presentColor);
        String color1 = "#" + colorPicker.getValue().toString().substring(2, 8);
        colorLabelOld.setStyle("-fx-background-color:" + color1 + ";");
        colorLabelNew.setStyle("-fx-background-color:" + color1 + ";");

        penValue.setText(shapeBuilder.getLineWidth() + "");

        System.out.println(shapeBuilder.isDashLine());
        dashLineBox.setSelected(shapeBuilder.isDashLine());

        if (shapeBuilder.getFillColor() == null) {
            checkBox.setSelected(false);
            sliderPen.setDisable(false);
            penValue.setDisable(false);
        } else {
            checkBox.setSelected(true);
            sliderPen.setDisable(true);
            penValue.setDisable(true);
        }

        type = shapeBuilder.getType();
        if (type == 0) {
            checkBox.setDisable(true);
        }
    }


}

class ShapeBuilder {
    private double width;
    private double height;
    private double lineWidth;
    private Color lineColor;
    private Color fillColor;
    private int type;
    private boolean isDashLine;

    public boolean isDashLine() {
        return isDashLine;
    }

    public void setDashLine(boolean dashLine) {
        isDashLine = dashLine;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public int getType() {
        return type;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    private ShapeBuilder(builder builder) {
        width = builder.width;
        height = builder.height;
        lineWidth = builder.lineWidth;
        lineColor = Color.web(builder.lineColor);
        isDashLine = builder.isDashLine;
        if (builder.fillColor.equals("")) {
            fillColor = null;
        } else {
            fillColor = Color.web(builder.fillColor);
        }

        type = builder.type;
    }

    static class builder {
        private double width;
        private double height;
        protected double lineWidth;
        protected String lineColor;
        protected String fillColor;
        protected int type;
        private boolean isDashLine;

        public builder setWidth(double width) {
            this.width = width;
            return this;
        }

        public builder setHeight(double height) {
            this.height = height;
            return this;
        }

        public builder setLineWidth(double lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public builder setLineColor(String lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public builder setFillColor(String fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        public builder setType(MyShape shape) {
            if (shape instanceof MyLine) {
                type = 0;
            } else {
                type = 1;
            }
            return this;
        }
        public builder setDashLine(boolean isDashLine) {
            this.isDashLine = isDashLine;
            return this;
        }

        public ShapeBuilder build() {
            return new ShapeBuilder(this);
        }
    }
}