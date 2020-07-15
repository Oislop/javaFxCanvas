package drawing.view.stage.pane;

import drawing.config.PngPathEnum;
import drawing.config.ShapeProperty;
import drawing.config.SizeEnum;
import drawing.model.*;
import drawing.utils.*;
import drawing.view.MainStage;
import drawing.view.stage.pane.stagetoolbarbuilder.BuildersLayersBar;
import drawing.view.stage.alert.AlertBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.*;

public class BoardPane implements StageBasePane {
    private static boolean startPaint = false;
    /**
     * 是否选中形状
     */
    private static boolean isChosedShape = false;
    /**
     * 鼠标点击次数
     */
    private static int clickedTimes = 0;

    // 鼠标点击位置
    private double x;
    private double y;

    /*
     * 鼠标右键点击位置
     */
    private double pasteX;
    private double pasteY;

    /**
     * 多边形点数组
     */
    double[] pointlist;

    private TextArea input = null;
    private static Shape shape = null;
    private static Shape dragged = null;
    private static Shape chosedShape = null;


    private static ArrayList<Shape> allChosedShapes;
    /**
     * 保存的的形状list
     */
    private static ArrayList<Shape> saveShapes;
    /**
     * 手绘多边形临时保存list
     */
    private ArrayList<Integer> polygonList;
    /**
     * Shapes保存复制List
     */
    private ArrayList<Shape> copyShapesList;

    /**
     * 右键菜单
     */
    ContextMenu contextMenu = ContextMenuPane.getInstance(this);

    /**
     * 舞台
     */
    private static MainStage stage;
    /**
     * 滚动面板
     */
    private Parent zoomPane;
    /**
     * VBox布局
     */
    private VBox layout;
    /**
     * 画板
     */
    private Canvas drawingCanvas;
    private Group group;
    /**
     * 图形上下文（画笔）
     */
    GraphicsContext gc;
    /**
     * 画板宽
     */
    public static int drawingCanvasWidth;
    /**
     * 画板高
     */
    public static int drawingCanvasHeight;
    /**
     * 是否按住shift键
     */
    private boolean shift = false;
    private boolean polygonNew = false;

    /**
     * 绘画类型
     */
    private String shapeType;
    /**
     * 是否填充形状
     */
    private boolean isFill;
    /**
     * 线宽度
     */
    private double lineWidth;
    /**
     * 形状填充颜色
     */
    private Color fillColor = null;
    /**
     * 线颜色
     */
    private Color lineColor;
    /**
     * 多边形边数
     */
    private int polygonN;

    /**
     * 选择后保存的shape
     */
    private boolean singleChoiceUndoShapes = false;
    private boolean multipleChoiceUndoShapes = false;
    private boolean multipleChoiceRedoShapes = false;
    private int undoShapesN = 0;
    private int redoShapesN = 0;

    /**
     * 撤销
     */
    private MementoCaretakerUndo mementoCaretakerUndo = new MementoCaretakerUndo();

    /**
     * 反撤销
     */
    private MementoCaretakerRedo mementoCaretakerRedo = new MementoCaretakerRedo();

    private boolean dashLine = false;

    private Context context;

    /**
     * 场景
     */
    private Scene scene;

    /**
     * 初始化Pane，list
     *
     * @param mainStage
     */
    @Override
    public void createPane(MainStage mainStage) {

        this.stage = mainStage;
        allChosedShapes = new ArrayList<>();
        saveShapes = new ArrayList<>();
        polygonList = new ArrayList<>();

        layout = new VBox();
        scene = new Scene(layout);
        initPane();
    }

    /**
     * 设置pane参数
     */
    @Override
    public void initPane() {
        // 初始化画板
        drawingCanvas = new Canvas(SizeEnum.CANVAS_WIDTH.getValue(), SizeEnum.CANVAS_HEIGHT.getValue());
        group = new Group();
        group.getChildren().add(drawingCanvas);
        zoomPane = CreateZoomPaneUtil.createZoomPane(group);

        layout.getChildren().add(zoomPane);
        VBox.setVgrow(zoomPane, Priority.ALWAYS);

        // 画笔绘制画板
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SizeEnum.CANVAS_WIDTH.getValue(), SizeEnum.CANVAS_HEIGHT.getValue());
        gc.restore();
        drawingCanvas.setCursor(Cursor.cursor("CROSSHAIR"));

        // 初始化画板宽高
        drawingCanvasWidth = (int) drawingCanvas.getWidth();
        drawingCanvasHeight = (int) drawingCanvas.getHeight();

        // 绑定事件监听
        bindCanvasEvents();
        handleKeyListener();
        setLayers(group);
    }

    /**
     * @return layout
     */
    @Override
    public VBox getPane() {
        return layout;
    }

    /**
     * 键盘监听事件
     */
    private void handleKeyListener() {
        stage.requestFocus();
        layout.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                shift = true;
            }
        });

        layout.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                shift = false;
            }

            if (event.getCode() == KeyCode.UP) {
                ArrayList<Shape> list = new ArrayList<>();
                for (Shape s : allChosedShapes) {
                    double[] pos = ((MyShape) s).getPos();
                    double[] points = new double[pos.length];
                    for (int i = 0; i < points.length; i += 2) {
                        points[i] = pos[i];
                        points[i + 1] = pos[i + 1] - 5;
                    }
                    Shape newShape = ((MyShape) s).deepCopy(points);
                    list.add(newShape);
                    group.getChildren().remove(s);
                    group.getChildren().add(newShape);
                    bindShapeEvents(newShape);
                    newShape.setEffect(ShadowUtil.setShadow());
                    //newShape.setVisible(false);
                    setLayers(group);
                }
                allChosedShapes = list;
            }
            if (event.getCode() == KeyCode.DOWN) {
                ArrayList<Shape> list = new ArrayList<>();
                for (Shape s : allChosedShapes) {
                    double[] pos = ((MyShape) s).getPos();
                    double[] points = new double[pos.length];
                    for (int i = 0; i < points.length; i += 2) {
                        points[i] = pos[i];
                        points[i + 1] = pos[i + 1] + 5;
                    }
                    Shape newShape = ((MyShape) s).deepCopy(points);
                    list.add(newShape);
                    group.getChildren().remove(s);
                    group.getChildren().add(newShape);
                    bindShapeEvents(newShape);
                    newShape.setEffect(ShadowUtil.setShadow());
                    //newShape.setVisible(false);
                    setLayers(group);
                }
                allChosedShapes = list;
            }
            if (event.getCode() == KeyCode.LEFT) {
                ArrayList<Shape> list = new ArrayList<>();
                for (Shape s : allChosedShapes) {
                    double[] pos = ((MyShape) s).getPos();
                    double[] points = new double[pos.length];
                    for (int i = 0; i < points.length; i += 2) {
                        points[i] = pos[i] - 5;
                        points[i + 1] = pos[i + 1];
                    }
                    Shape newShape = ((MyShape) s).deepCopy(points);
                    list.add(newShape);
                    group.getChildren().remove(s);
                    group.getChildren().add(newShape);
                    bindShapeEvents(newShape);
                    newShape.setEffect(ShadowUtil.setShadow());
                    setLayers(group);
                }
                allChosedShapes = list;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                ArrayList<Shape> list = new ArrayList<>();
                for (Shape s : allChosedShapes) {
                    double[] pos = ((MyShape) s).getPos();
                    double[] points = new double[pos.length];
                    for (int i = 0; i < points.length; i += 2) {
                        points[i] = pos[i] + 5;
                        points[i + 1] = pos[i + 1];
                    }
                    Shape newShape = ((MyShape) s).deepCopy(points);
                    list.add(newShape);
                    group.getChildren().remove(s);
                    group.getChildren().add(newShape);
                    bindShapeEvents(newShape);
                    newShape.setEffect(ShadowUtil.setShadow());
                    //newShape.setVisible(false);
                    setLayers(group);
                }
                allChosedShapes = list;
            }
        });

    }

    /**
     * 设置绘制属性
     */
    private void setProperty() {
        dashLine = ShapeProperty.isIsDashLine();
        polygonN = ShapeProperty.getPolygonN();
        shapeType = ShapeProperty.toolName;
        isFill = ShapeProperty.borderSize.equals("FILL");
        lineColor = ShapeProperty.color;
        if (isFill) {
            lineWidth = 1;
            fillColor = lineColor;
        } else {
            if (shapeType == "PEN" || shapeType == "RUBBER" || shapeType == "LINE") {
                lineWidth = ShapeProperty.lineSize;
            } else
                lineWidth = Double.parseDouble(ShapeProperty.borderSize);
            fillColor = null;
        }
    }

    private Rectangle rectangle = new Rectangle();
    private static Bloom bloom = new Bloom(0.3);
    private LinkedList<Shape> selected = new LinkedList<>();
    private Shape nowShape1;


    public void addSelected(Shape ashape) {
        if (!selected.contains(ashape))
            selected.add(ashape);
        if (selected.size() == 1)
            nowShape1 = ashape;
        else
            nowShape1 = null;
        System.out.println("selected:" + selected);
    }

    public void deleteSelected(Shape ashape) {
        selected.remove(ashape);
        if (selected.size() == 1)
            nowShape1 = selected.get(0);
        else
            nowShape1 = null;
        System.out.println("deleteSelected");
    }

    private boolean inRange1(double x1, double y1, double x2, double y2, Shape rangeShape) {

        if (rangeShape instanceof MyRectangle) {
            double r1 = ((MyRectangle) rangeShape).getPosX();
            double r2 = ((MyRectangle) rangeShape).getPosY();
            double r3 = ((MyRectangle) rangeShape).getPosX() + ((MyRectangle) rangeShape).getMyWidth();
            double r4 = ((MyRectangle) rangeShape).getPosY() + ((MyRectangle) rangeShape).getMyHeight();
            double minX = Math.min(r1, r3);
            double maxY = Math.max(r2, r4);

            double minRX = Math.min(x1, x2);
            double maxRY = Math.max(y1, y2);
            if (minRX < minX && maxY < maxRY) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void choose(double x1, double y1, double x2, double y2) {
        for (int i = 1; i < group.getChildren().size(); i++) {
            if (Geometry.inRange(x1, y1, x2, y2, group.getChildren().get(i))) {
                addSelected((Shape) group.getChildren().get(i));
                group.getChildren().get(i).setEffect(ShadowUtil.setShadow());
            } else {
                System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
                deleteSelected((Shape) group.getChildren().get(i));
                group.getChildren().get(i).setStyle(null);
                group.getChildren().get(i).setEffect(null);
            }
        }
    }

    public void change() {
        if (selected.size() == 0) {
            System.out.println("remove sel");
            group.getChildren().remove(rectangle);
        }
        allChosedShapes = new ArrayList<>();
        if (selected.size() == 1) {
            chosedShape = selected.get(0);
            chosedShape.setEffect(ShadowUtil.setShadow());
            allChosedShapes.add(chosedShape);
            shape = null;
            // System.out.println("selected.get(0): "+ selected.get(0));
            // System.out.println("chosedShape: " + chosedShape);
            // System.out.println("allChosedShapes: " + allChosedShapes);
            // System.out.println("change group: " + group.getChildren());
        } else if (selected.size() > 1) {
            for (Shape s : selected) {
                s.setEffect(ShadowUtil.setShadow());
                allChosedShapes.add(s);
            }
        }
    }

    /**
     * 监听鼠标事件
     */
    protected void bindCanvasEvents() {
        // 实时绘制鼠标坐标
        drawingCanvas.setOnMouseMoved(mouseMoveEvt -> {
            InfoDraw.setText(String.format("X: %.1f px, Y: %.1f px ", mouseMoveEvt.getX(), mouseMoveEvt.getY()));
        });

        // 鼠标单击事件
        drawingCanvas.setOnMouseClicked(mouseClickedEvt -> {

            if (mouseClickedEvt.getClickCount() == 2 && shapeType == "POLYGON1") {
                int size = polygonList.size();
                double[] points = new double[size];
                for (int i = 0; i < size; i++) {
                    points[i] = polygonList.get(i);
                }
                for (int i = 0; i < size / 2; i++) {
                    //undo();
                    Shape shape = (Shape) group.getChildren().get(group.getChildren().size() - 1);
                    mementoCaretakerUndo.delUndoStack(shape);
                    group.getChildren().remove(shape);
                }
                Shape shapeObj = new MyPolygon(dashLine, 0.0, lineWidth, lineColor, fillColor,
                        points);

                group.getChildren().add(shapeObj);
                Node tmp = group.getChildren().get(group.getChildren().size() - 1);
                mementoCaretakerUndo.setUndoStack((Shape) tmp);
                this.bindShapeEvents(tmp);
                polygonList.clear();
                polygonNew = true;

                setLayers(group);
            }

        });

        drawingCanvas.setOnMousePressed(mousePressedEvt -> {

            // 隐藏右键菜单
            contextMenu.hide();
            // 显示右键菜单
            if (mousePressedEvt.isSecondaryButtonDown()) {
                drawingCanvas.setOnContextMenuRequested(event -> contextMenu.show(drawingCanvas,
                        mousePressedEvt.getScreenX(), mousePressedEvt.getScreenY()));
                this.pasteX = mousePressedEvt.getX();
                this.pasteY = mousePressedEvt.getY();
                return;
            }

            setProperty();

            if (!shapeType.equals("POLYGON1")) {
                ShapeProperty.setDrawPolygon(false);
            }

            this.startPaint = true;

            if (ShapeProperty.drawPolygon && !polygonNew) {
                return;
            }

            polygonNew = false;
            this.x = mousePressedEvt.getX();
            this.y = mousePressedEvt.getY();


            if (shapeType.equals("TEXT") && this.clickedTimes == 0) {
                if (input != null) {
                    Shape shapeObj = new MyText(
                            input.getTranslateX(),
                            input.getTranslateY() + input.getHeight(),
                            input.getText(),
                            new Font(ShapeProperty.fontName, ShapeProperty.fontSize),
                            0.5,
                            lineColor,
                            fillColor);

                    if (input.getText().equals("")) {
                        return;
                    }

                    group.getChildren().remove(input);
                    group.getChildren().add(shapeObj);
                    this.bindShapeEvents(shapeObj);
                    mementoCaretakerUndo.setUndoStack((Shape) shapeObj);
                    input = null;
                } else {
                    input = new TextArea();
                    input.setPrefHeight(ShapeProperty.fontSize);
                    input.setPrefWidth(3);
                    input.setStyle("-fx-background-color:Transparent");
                    input.setOnKeyPressed(e -> {
                        input.setFont(new Font(ShapeProperty.fontName, ShapeProperty.fontSize));
                        if (e.getCode() == KeyCode.ENTER) {
                            input.setPrefHeight(input.getHeight() + ShapeProperty.fontSize);
                        } else {
                            input.setPrefWidth(input.getWidth() + 10);
                        }
                    });
                    input.setTranslateX(x);
                    input.setTranslateY(y);
                    group.getChildren().add(input);
                    setLayers(group);
                }
            }

        });

        // 鼠标离开事件
        drawingCanvas.setOnMouseExited(mouseExitedEvt -> {
            InfoDraw.setText("");
            return;
        });

        // 鼠标拖动事件
        drawingCanvas.setOnMouseDragged(mouseDraggedEvt -> {
            InfoDraw.setText(String.format("X: %.1f px, Y: %.1f px ", mouseDraggedEvt.getX(), mouseDraggedEvt.getY()));

            if (this.startPaint && !isChosedShape) {
                if (group.getChildren().size() >= 1 && this.shape != null) {
                    group.getChildren().remove(this.shape);
                }

                Shape shapeObj = null;

                double startX = Math.min(this.x, mouseDraggedEvt.getX());
                double startY = Math.min(this.y, mouseDraggedEvt.getY());

                boolean isPen = false;

                if (shapeType.equals("MOUSE") && mouseDraggedEvt.getButton().equals(MouseButton.PRIMARY)) {
                    for(Shape s : allChosedShapes) {
                        s.setEffect(null);
                    }
                    allChosedShapes = new ArrayList<>();
                    shapeObj = new MyRectangle(dashLine, 0.0,
                            false, startX, startY,
                            Math.abs(mouseDraggedEvt.getX() - this.x),
                            Math.abs(mouseDraggedEvt.getY() - this.y),
                            lineWidth, lineColor, Color.SKYBLUE);
                    shapeObj.setOpacity(0.3);

                    rectangle = (Rectangle) shapeObj;
                    //shapeObj = rectangle;
                }

                if (shapeType.equals("PEN")) {
                    // 画笔
                    isPen = true;
                    context = new Context(new MyPen(
                            dashLine,
                            this.x,
                            this.y,
                            mouseDraggedEvt.getX(),
                            mouseDraggedEvt.getY(),
                            lineWidth,
                            lineColor,
                            fillColor));
                    shapeObj = context.executeShape();
                    x = mouseDraggedEvt.getX();
                    y = mouseDraggedEvt.getY();
                    group.getChildren().add(shapeObj);

                }

                if (shapeType.equals("RUBBER")) {
                    // 橡皮擦
                    isPen = true;
                    shapeObj = new MyPen(
                            dashLine,
                            this.x,
                            this.y,
                            mouseDraggedEvt.getX(),
                            mouseDraggedEvt.getY(),
                            lineWidth,
                            Color.WHITE,
                            fillColor);
                    x = mouseDraggedEvt.getX();
                    y = mouseDraggedEvt.getY();
                    group.getChildren().add(shapeObj);
                }

                if (shapeType.equals("SQUARE")) {
                    if (shift) {
                        //画长方形
                        context = new Context(new MyRectangle(dashLine, 0.0,
                                false, startX, startY,
                                Math.abs(mouseDraggedEvt.getX() - this.x),
                                Math.abs(mouseDraggedEvt.getY() - this.y),
                                lineWidth, lineColor, fillColor));
                        shapeObj = context.executeShape();
                    } else {
                        //画正方形
                        shapeObj = new MySquare(dashLine, 0.0, false, startX, startY,
                                Math.max(Math.abs(mouseDraggedEvt.getX() - this.x),
                                        Math.abs(mouseDraggedEvt.getY() - this.y)),
                                lineWidth, lineColor, fillColor);
                    }

                }

                if (shapeType.equals("SQUAREY")) {
                    if (shift) {
                        //画长方形
                        shapeObj = new MyRectangle(dashLine, 0.0, true, startX, startY,
                                Math.abs(mouseDraggedEvt.getX() - this.x), Math.abs(mouseDraggedEvt.getY() - this.y),
                                lineWidth, lineColor, fillColor);
                    } else {
                        //画正方形
                        shapeObj = new MySquare(dashLine, 0.0, true, startX, startY,
                                Math.max(Math.abs(mouseDraggedEvt.getX() - this.x), Math.abs(mouseDraggedEvt.getY() - this.y)),
                                lineWidth, lineColor, fillColor);
                    }
                }

                if (shapeType.equals("OVAL")) {
                    if (shift) {
                        //画椭圆
                        shapeObj = new MyEllipse(
                                dashLine,
                                0.0,
                                this.x,
                                this.y,
                                Math.abs(mouseDraggedEvt.getX() - this.x), Math.abs(mouseDraggedEvt.getY() - this.y),
                                lineWidth, lineColor, fillColor);
                    } else {
                        //画圆形
                        shapeObj = new MyCircle(
                                dashLine,
                                this.x,
                                this.y,
                                Math.max(Math.abs(mouseDraggedEvt.getX() - this.x), Math.abs(mouseDraggedEvt.getY() - this.y)),
                                lineWidth, lineColor, fillColor);
                    }
                }

                if (shapeType.equals("LINE")) {
                    //画直线
                    shapeObj = new MyLine(
                            dashLine,
                            this.x,
                            this.y,
                            mouseDraggedEvt.getX(), mouseDraggedEvt.getY(),
                            lineWidth, lineColor, fillColor);
                }

                if (shapeType.equals("POLYGON")) {
                    //画多边形
                    int cnt = 2;
                    pointlist = new double[polygonN * 2];
                    pointlist[0] = x;
                    pointlist[1] = y;
                    for (int i = 1; i < polygonN; i++) {
                        double[] p = PolygonUtil.nextPoint(x, y,
                                (int) Math.abs(mouseDraggedEvt.getX() - this.x),
                                ((2 * Math.PI) / polygonN) * i);
                        pointlist[cnt++] = p[0];
                        pointlist[cnt++] = p[1];
                    }

                    shapeObj = new MyPolygon(
                            dashLine,
                            0.0,
                            lineWidth, lineColor, fillColor,
                            pointlist);
                }

                if (shapeType.equals("POLYGON1")) {
                    //画多边形
                    ShapeProperty.setDrawPolygon(true);
                    Path path = new Path(new MoveTo(x, y),
                            new LineTo(mouseDraggedEvt.getX(), mouseDraggedEvt.getY()));

                    shapeObj = path;

                }

                if (shapeObj != null) {
                    if (isPen) {
                        //this.shape = shapeObj;
                        mementoCaretakerUndo.setUndoStack(shapeObj);
                        return;
                    }
                    group.getChildren().add(shapeObj);
                    // if (!shapeType.equals("POLYGON1") && !shapeType.equals("PEN") && !shapeType.equals("MOUSE")) {
                    //     setLayers(group);
                    // }

                    this.shape = shapeObj;
                }
            }

        });

        // 鼠标释放事件
        drawingCanvas.setOnMouseReleased(mouseReleasedEvt -> {

            if (this.startPaint && shapeType.equals("MOUSE") && mouseReleasedEvt.getButton().equals(MouseButton.PRIMARY)) {

                group.getChildren().remove(rectangle);
                selected.remove(rectangle);

                choose(x, y, mouseReleasedEvt.getX(), mouseReleasedEvt.getY());
                change();
                selected = new LinkedList<>();
                return;
            }


            this.startPaint = false;
            if (this.shape == null) {
                return;
            }
            if (this.shape == null) {
                group.getChildren().add(this.shape);
            }

            if (ShapeProperty.drawPolygon) {
                x = mouseReleasedEvt.getX();
                y = mouseReleasedEvt.getY();
                polygonList.add((int) x);
                polygonList.add((int) y);
                Node tmp = group.getChildren().get(group.getChildren().size() - 1);
                mementoCaretakerUndo.setUndoStack((Shape) tmp);
                this.shape = null;
                return;
            }

            Node tmp = group.getChildren().get(group.getChildren().size() - 1);
            System.out.println(tmp);
            mementoCaretakerUndo.setUndoStack((Shape) tmp);
            setLayers(group);
            this.bindShapeEvents(tmp);
            this.shape = null;
        });

    }

    /**
     * 图形监听事件
     *
     * @param shape
     */
    protected void bindShapeEvents(Node shape) {

        // 图形选择事件
        shape.setOnMousePressed(mousePressedEvt -> {
            if (this.chosedShape != shape) {
                this.clickedTimes = 0;
            }

            BoardPane.clickedTimes++;
            if (BoardPane.clickedTimes == 1) {
                this.x = mousePressedEvt.getX();
                this.y = mousePressedEvt.getY();
                shape.setEffect(ShadowUtil.setShadow());
                if (!this.allChosedShapes.contains(shape)) {
                    this.allChosedShapes.add((Shape) shape);
                }
                this.chosedShape = (Shape) shape;
                return;
            }

            if (!singleChoiceUndoShapes) {
                mementoCaretakerUndo.setUndoStack((Shape) shape);
                singleChoiceUndoShapes = true;
            }

            if (BoardPane.clickedTimes == 2) {
                BoardPane.clickedTimes = 0;
                BoardPane.isChosedShape = true;
                this.chosedShape = (Shape) shape;
                return;
            }

            if (input != null) {
                group.getChildren().remove(input);
                setLayers(group);
            }

        });

        // 图形拖动事件
        shape.setOnMouseDragged(mouseDraggedEvt -> {
            if (BoardPane.isChosedShape) {
                drawingCanvas.setCursor(Cursor.CLOSED_HAND);
                if (this.dragged != null && group.getChildren().contains(this.dragged)) {
                    group.getChildren().remove(this.dragged);
                }
                // 鼠标偏移量
                double offsetX = mouseDraggedEvt.getX() - this.x;
                double offsetY = mouseDraggedEvt.getY() - this.y;

                double[] pos = ((MyShape) shape).getPos();
                double[] points = new double[pos.length];
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i] + offsetX;
                    points[i + 1] = pos[i + 1] + offsetY;
                }

                Shape newShape = ((MyShape) shape).deepCopy(points);
                this.dragged = newShape;
                this.dragged.setEffect(ShadowUtil.setShadow());
                group.getChildren().add(newShape);
                shape.setVisible(false);
                //this.shape = newShape;
            }
        });

        // 图形释放事件
        shape.setOnMouseReleased(mouseReleaseEvt -> {
            drawingCanvas.setCursor(Cursor.CROSSHAIR);
            if (!BoardPane.isChosedShape) {
                return;
            }

            if (this.dragged != null) {
                this.dragged.setEffect(null);
                group.getChildren().remove(this.chosedShape);
                this.allChosedShapes.remove(this.chosedShape);
            } else {
                this.chosedShape.setEffect(null);
            }

            if (this.allChosedShapes.contains(this.chosedShape) && this.chosedShape.getEffect() == null) {
                this.allChosedShapes.remove(this.chosedShape);
            }
            if (group.getChildren().contains(this.chosedShape))
                System.out.println("Yes");

            Node tmp = group.getChildren().get(group.getChildren().size() - 1);
            mementoCaretakerUndo.setUndoStack((Shape) tmp);
            if (!shapeType.equals("PEN")) {
                setLayers(group);
            }

            this.bindShapeEvents(tmp);
            BoardPane.clickedTimes = 0;
            BoardPane.isChosedShape = false;
            this.dragged = null;
        });

    }

    /**
     * 加载文件到画板
     */
    public void addShapes(ArrayList<Shape> shapes) {
        for (Shape s : shapes) {
            group.getChildren().add(s);
            setLayers(group);
            mementoCaretakerUndo.setUndoStack(s);
            this.bindShapeEvents(s);
        }
    }

    /**
     * 上对齐
     */
    protected void alignmentTopShapes() {
        double top = Double.MAX_VALUE;
        double heigh = 0;
        double pol = 0;
        for (Shape s : this.allChosedShapes) {
            double[] pos = ((MyShape) s).getPos();
            if (s instanceof MyCircle) {
                heigh = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                heigh = ((MyEllipse) s).getMyRadiusY();
            } else if (s instanceof MyPolygon) {
                Double[] polPos = new Double[pos.length / 2];
                int cnt = 0;
                for (int i = 1; i < pos.length; i += 2) {
                    polPos[cnt++] = pos[i];
                }
                Arrays.sort(polPos);
                heigh = polPos[0];
                pol = heigh;
            }
            if (s instanceof MyPolygon) {
                top = Math.min(top, heigh);
            } else {
                top = Math.min(top, pos[1] - heigh);
            }
        }

        for (Shape s : this.allChosedShapes) {
            double newH = 0;
            if (s instanceof MyCircle) {
                newH = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                newH = ((MyEllipse) s).getMyRadiusY();
            }
            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double polY = pol - top;
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    if (top == pol) {
                        points[i + 1] = pos[i + 1];
                    } else {
                        points[i + 1] = pos[i + 1] - polY;
                    }
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    points[i + 1] = top + newH;
                }
            }

            Shape newShape = ((MyShape) s).deepCopy(points);
            delLastShape(s);
            {
                mementoCaretakerUndo.setUndoStack(newShape);
                undoShapesN = allChosedShapes.size();
                multipleChoiceUndoShapes = true;
            }
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 下对齐
     */
    protected void aligmentBottomShapes() {
        double bottom = Double.MIN_VALUE;
        double heigh = 0;
        double pol = 0;
        for (Shape s : this.allChosedShapes) {
            double[] pos = ((MyShape) s).getPos();
            if (s instanceof MyRectangle) {
                heigh = ((MyRectangle) s).getMyHeight();
            } else if (s instanceof MyCircle) {
                heigh = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                heigh = ((MyEllipse) s).getMyRadiusY();
            } else if (s instanceof MyPolygon) {
                Double[] polPos = new Double[pos.length / 2];
                int cnt = 0;
                for (int i = 1; i < pos.length; i += 2) {
                    polPos[cnt++] = pos[i];
                }
                Arrays.sort(polPos, Collections.reverseOrder());
                heigh = polPos[0];
                pol = heigh;
            }
            if (s instanceof MyPolygon) {
                bottom = Math.max(bottom, heigh);
            } else {
                bottom = Math.max(bottom, pos[1] + heigh);
            }
        }

        for (Shape s : this.allChosedShapes) {
            double newH = 0;
            if (s instanceof MyRectangle) {
                newH = ((MyRectangle) s).getMyHeight();
            } else if (s instanceof MyCircle) {
                newH = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                newH = ((MyEllipse) s).getMyRadiusY();
            }

            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double polY = bottom - pol;
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    if (bottom == pol) {
                        points[i + 1] = pos[i + 1];
                    } else {
                        points[i + 1] = pos[i + 1] + polY;
                    }
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    points[i + 1] = bottom - newH;
                }
            }

            Shape newShape = ((MyShape) s).deepCopy(points);
            delLastShape(s);
            {
                mementoCaretakerUndo.setUndoStack(newShape);
                undoShapesN = allChosedShapes.size();
                multipleChoiceUndoShapes = true;
            }
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 左对齐
     */
    protected void aligmentLeftShapes() {
        double left = Double.MAX_VALUE;
        double heigh = 0;
        double polX = 0; // 多边形最左边的点
        for (Shape s : this.allChosedShapes) {
            double[] pos = ((MyShape) s).getPos();
            if (s instanceof MyCircle) {
                heigh = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                heigh = ((MyEllipse) s).getMyRadiusY();
            } else if (s instanceof MyPolygon) {
                double[] polPos = new double[pos.length / 2];
                int cnt = 0;
                for (int i = 0; i < pos.length; i += 2) {
                    polPos[cnt++] = pos[i];
                }
                Arrays.sort(polPos);
                heigh = polPos[0];
                polX = heigh;
            }
            if (s instanceof MyPolygon) {
                left = Math.min(left, heigh);
            } else {
                left = Math.min(left, pos[0] + heigh);
            }
        }

        for (Shape s : this.allChosedShapes) {
            double newH = 0;
            if (s instanceof MyCircle) {
                newH = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                newH = ((MyEllipse) s).getMyRadiusY();
            }
            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double polY = polX - left;
                for (int i = 0; i < points.length; i += 2) {
                    if (left == polX) {
                        points[i] = pos[i];
                    } else {
                        points[i] = pos[i] - polY;
                    }
                    points[i + 1] = pos[i + 1];
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = left + newH;
                    points[i + 1] = pos[i + 1];
                }
            }

            Shape newShape = ((MyShape) s).deepCopy(points);
            delLastShape(s);
            {
                mementoCaretakerUndo.setUndoStack(newShape);
                undoShapesN = allChosedShapes.size();
                multipleChoiceUndoShapes = true;
            }
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 右对齐
     */
    protected void aligmentRightShapes() {
        double right = Double.MIN_VALUE;
        double heigh = 0;
        double pol = 0; // 多边形最左边的点
        for (Shape s : this.allChosedShapes) {
            double[] pos = ((MyShape) s).getPos();
            if (s instanceof MyRectangle) {
                heigh = ((MyRectangle) s).getMyHeight();
            } else if (s instanceof MyCircle) {
                heigh = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                heigh = ((MyEllipse) s).getMyRadiusY();
            } else if (s instanceof MyPolygon) {
                Double[] polPos = new Double[pos.length / 2];
                int cnt = 0;
                for (int i = 0; i < pos.length; i += 2) {
                    polPos[cnt++] = pos[i];
                }
                Arrays.sort(polPos, Collections.reverseOrder());
                heigh = polPos[0];
                pol = heigh;
            }
            if (s instanceof MyPolygon) {
                right = Math.max(right, heigh);
            } else {
                right = Math.max(right, pos[0] + heigh);
            }
        }

        for (Shape s : this.allChosedShapes) {
            double newH = 0;
            if (s instanceof MyRectangle) {
                newH = ((MyRectangle) s).getMyHeight();
            } else if (s instanceof MyCircle) {
                newH = ((MyCircle) s).getMyRadius();
            } else if (s instanceof MyEllipse) {
                newH = ((MyEllipse) s).getMyRadiusY();
            }
            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double polY = pol + right;
                for (int i = 0; i < points.length; i += 2) {
                    if (right == pol) {
                        points[i] = pos[i];
                    } else {
                        points[i] = pos[i] + polY;
                    }
                    points[i + 1] = pos[i + 1];
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = right - newH;
                    points[i + 1] = pos[i + 1];
                }
            }

            Shape newShape = ((MyShape) s).deepCopy(points);
            delLastShape(s);
            {
                mementoCaretakerUndo.setUndoStack(newShape);
                undoShapesN = allChosedShapes.size();
                multipleChoiceUndoShapes = true;
            }
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 中心对齐
     */
    protected void aligmentCenterShapes() {
        double heigh = 0;
        double polMinY = 0;
        double polMaxY = 0;
        double pol = 0;
        for (Shape s : this.allChosedShapes) {
            double[] pos = ((MyShape) s).getPos();
            if (s instanceof MyPolygon) {
                Double[] polPos = new Double[pos.length / 2];
                int cnt = 0;
                for (int i = 1; i < pos.length; i += 2) {
                    polPos[cnt++] = pos[i];
                }
                Arrays.sort(polPos);
                polMinY = polPos[0];
                polMaxY = polPos[cnt - 1];
                pol = Math.abs((polMinY - polMaxY) / 2);
            }
        }

        for (Shape s : this.allChosedShapes) {
            double top = drawingCanvasHeight / 2;
            if (s instanceof MyRectangle) {
                heigh = ((MyRectangle) s).getMyHeight() / 2;
                top = top - heigh;
            }
            double newH = 0;
            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double polY = pol + polMinY - top;
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    if (top == pol) {
                        points[i + 1] = pos[i + 1];
                    } else {
                        points[i + 1] = pos[i + 1] - polY;
                    }
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = pos[i];
                    points[i + 1] = top + newH;
                }
            }
            Shape newShape = ((MyShape) s).deepCopy(points);
            delLastShape(s);
            {
                mementoCaretakerUndo.setUndoStack(newShape);
                undoShapesN = allChosedShapes.size();
                multipleChoiceUndoShapes = true;
            }
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 撤销操作
     */
    public void undo() {
        if (mementoCaretakerUndo.getUndoStack().empty()) {
            System.out.println("undo stack empty!");
            return;
        }
        // 处理多选图形撤销
        if (multipleChoiceUndoShapes) {
            System.out.println(mementoCaretakerUndo.getUndoStack());
            Stack<Shape> newStack = new Stack<>();
            // 删除当前显示图层
            for (int i = 0; i < undoShapesN; i++) {
                Shape undoShapeWithPop = mementoCaretakerUndo.getShapeWithPop();
                mementoCaretakerRedo.setRedoStack(undoShapeWithPop);
                group.getChildren().remove(undoShapeWithPop);
            }

            // 添加修改前之前保存的图形
            for (int i = 0; i < undoShapesN; i++) {
                Shape newObj = mementoCaretakerUndo.getShapeWithPop();
                newStack.push(newObj);
                group.getChildren().add(newObj);
                setLayers(group);
                newObj.setVisible(true);
                newObj.setEffect(null);
            }
            // 把修改前之前保存的图形压栈
            while (!newStack.empty()) {
                mementoCaretakerUndo.setUndoStack(newStack.pop());
            }

            multipleChoiceRedoShapes = true;
            redoShapesN = undoShapesN;
            multipleChoiceUndoShapes = false;
            undoShapesN = 0;
            allChosedShapes = new ArrayList<>();
            return;
        }

        System.out.println(mementoCaretakerUndo.getUndoStack());
        Shape undoShape = mementoCaretakerUndo.getShapeWithPop();
        mementoCaretakerRedo.setRedoStack(undoShape);
        group.getChildren().remove(undoShape);
        setLayers(group);
        if (singleChoiceUndoShapes) {
            Shape newObj = mementoCaretakerUndo.getShapeWithPop();
            mementoCaretakerRedo.setRedoStack(newObj);
            group.getChildren().add(newObj);
            setLayers(group);
            newObj.setVisible(true);
            newObj.setEffect(null);
            singleChoiceUndoShapes = false;
        }
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 重做操作
     */
    public void redo() {
        if (mementoCaretakerRedo.getUndoStack().isEmpty()) {
            System.out.println("redo stack empty!");
            return;
        }

        if (multipleChoiceRedoShapes) {
            System.out.println(mementoCaretakerRedo.getUndoStack());
            Stack<Shape> newStack = new Stack<>();

            // 删除当前显示图层
            for (int i = 0; i < redoShapesN; i++) {
                Shape undoShapeWithPop = mementoCaretakerUndo.getShapeWithPop();
                //mementoCaretakerUndo.setUndoStack(undoShapeWithPop);
                newStack.push(undoShapeWithPop);
                group.getChildren().remove(undoShapeWithPop);
            }

            // 添加修改前之前保存的图形
            for (int i = 0; i < redoShapesN; i++) {
                Shape newObj = mementoCaretakerRedo.getShapeWithPop();

                group.getChildren().add(newObj);
                setLayers(group);
                newObj.setVisible(true);
                newObj.setEffect(null);
            }
            // 把修改前之前保存的图形压栈
            while (!newStack.empty()) {
                mementoCaretakerUndo.setUndoStack(newStack.pop());
            }

            //multipleChoiceUndoShapes = true;
            //undoShapesN = redoShapesN;

            multipleChoiceRedoShapes = false;
            redoShapesN = 0;
            allChosedShapes = new ArrayList<>();
            return;
        }

        Shape redoShape = mementoCaretakerRedo.getShapeWithPop();
        group.getChildren().remove(redoShape);
        mementoCaretakerUndo.setUndoStack(redoShape);
        group.getChildren().add(redoShape);
        setLayers(group);
        redoShape.setVisible(true);
        redoShape.setEffect(null);

        allChosedShapes = new ArrayList<>();
    }

    /**
     * 删除上一层
     */
    private void delLastShape(Shape delshape) {
        group.getChildren().remove(delshape);
        setLayers(group);
    }

    /**
     * 删除选择图形
     */
    protected void deleteChosedShapes() {
        if (getAllChosedShapes().size() == 0) {
            NoChosedShapesAlert.alert();
            return;
        }
        // if(allChosedShapes.size() > 1){
        //     multipleChoiceUndoShapes = true;
        // } else {
        //     singleChoiceUndoShapes = true;
        // }
        for (Shape s : this.allChosedShapes) {
            group.getChildren().remove(s);
        }
        //allChosedShapes = new ArrayList<>();
        setLayers(group);
    }

    /**
     * 复制选择图形
     */
    protected void copyChosedShapes() {
        if (getAllChosedShapes().size() == 0) {
            //NoChosedShapesAlert.alert();
            new AlertBuilder.builder(new Alert(Alert.AlertType.WARNING))
                    .setTitle("警告")
                    .setHeaderText(null)
                    .setStageStyle(StageStyle.DECORATED)
                    .setContentText("未选择图形！")
                    .setStage()
                    .setIconPath(PngPathEnum.WARNING.toString())
                    .build();
            return;
        }
        copyShapesList = new ArrayList<>();
        for (Shape s : this.allChosedShapes) {
            copyShapesList.add(s);
        }
    }

    /**
     * 粘贴选择图形
     */
    protected void pasteChosedShapes() {
        double xx;
        double yy;
        if (pasteX == 0 && pasteY == 0) {
            // 之前鼠标位置
            xx = x;
            yy = y;
        } else {
            // 鼠标右键位置
            xx = pasteX;
            yy = pasteY;
        }

        double[][] pointXY = new double[10][100];
        double[][] newPointXY = new double[10][100];
        int cnt = 0;
        for (int i = 0; i < copyShapesList.size(); i++) {
            double[] pos = ((MyShape) copyShapesList.get(i)).getPos();
            for (int j = 0; j < pos.length; j += 2) {
                pointXY[cnt][j] = pos[j];
                pointXY[cnt++][j + 1] = pos[j + 1];
            }
        }
        newPointXY[0][0] = 0;
        newPointXY[0][1] = 0;
        for (int i = 1; i < cnt; i++) {
            if (i + 1 > cnt)
                break;
            newPointXY[i][0] = pointXY[i][0] - pointXY[i - 1][0];
            newPointXY[i][1] = pointXY[i][1] - pointXY[i - 1][1];
        }

        int cnt1 = 0;
        for (Shape s : this.copyShapesList) {
            double[] pos = ((MyShape) s).getPos();
            double[] points = new double[pos.length];
            if (s instanceof MyPolygon) {
                double ploX = xx - pos[0];
                double ploY = yy - pos[1];
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = ploX + pos[i];
                    points[i + 1] = ploY + pos[i + 1];
                }
            } else {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = xx + newPointXY[cnt1][0];
                    points[i + 1] = yy + newPointXY[cnt1++][1];
                }
            }
            Shape newShape = ((MyShape) s).deepCopy(points);
            group.getChildren().add(newShape);
            this.bindShapeEvents(newShape);
            s.setEffect(null);
        }
        setLayers(group);
        allChosedShapes = new ArrayList<>();
    }

    /**
     * 重置画板
     */
    public void clear() {
        group.getChildren().clear();
        mementoCaretakerUndo = new MementoCaretakerUndo();
        mementoCaretakerRedo = new MementoCaretakerRedo();
        selected = new LinkedList<>();
        saveShapes = new ArrayList<>();
        allChosedShapes = new ArrayList<>();
        chosedShape = null;
        undoShapesN = 0;
        multipleChoiceUndoShapes = false;
        singleChoiceUndoShapes = false;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SizeEnum.CANVAS_WIDTH.getValue(), SizeEnum.CANVAS_HEIGHT.getValue());
        gc.restore();
        group.getChildren().add(drawingCanvas);
        setLayers(group);
    }

    /**
     * 编辑图形
     */
    protected void editShapes() {

        Platform.runLater(() -> {
            if (allChosedShapes.size() > 1) {
                EditAllPane editPane = new EditAllPane(allChosedShapes, stage);
                ArrayList<Shape> newEditShapes = editPane.getNewShapes();
                if (newEditShapes.size() == 0)
                    return;
                for (Shape s : allChosedShapes) {
                    multipleChoiceUndoShapes = true;
                    delLastShape(s);
                }
                undoShapesN = newEditShapes.size();
                for (Shape s : newEditShapes) {
                    group.getChildren().add(s);
                    mementoCaretakerUndo.setUndoStack(s);
                    bindShapeEvents(s);
                }
                setLayers(group);
                allChosedShapes = new ArrayList<>();
                return;
            }

            EditPane editPane = new EditPane(chosedShape, stage);
            Shape newObj = editPane.handlePane();
            if (newObj == null)
                return;
            {
                mementoCaretakerUndo.setUndoStack(chosedShape);
                singleChoiceUndoShapes = true;
            }

            delLastShape(chosedShape);
            group.getChildren().add(newObj);
            setLayers(group);
            mementoCaretakerUndo.setUndoStack(newObj);
            bindShapeEvents(newObj);
        });

    }

    private void setLayers(Group group) {
        BuildersLayersBar directorLayersBar = ToolBarPane.getLayersBar();
        Platform.runLater(() -> {
            directorLayersBar.setLayers(group);
        });
    }

    /**
     * 旧右键菜单类
     */
    @Deprecated
    protected void noChosedShapesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.DECORATED);
        alert.setContentText("未选择图形！");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(PngPathEnum.WARNING.toString()));
        alert.showAndWait();
    }

    /**
     * @return allChosedShapes
     */
    public static ArrayList<Shape> getAllChosedShapes() {
        return allChosedShapes;
    }

    public Scene getScene() {
        return scene;
    }

    public ArrayList<Shape> getSaveShapes() {
        saveShapes = new ArrayList<>();
        for (int i = 1; i < group.getChildren().size(); i++) {
            saveShapes.add((Shape) group.getChildren().get(i));
        }
        return saveShapes;
    }

    public static Stage getStage() {
        return stage;
    }

    public Group getGroup() {
        return group;
    }

    public static int getDrawingCanvasWidth() {
        return drawingCanvasWidth;
    }

    public static int getDrawingCanvasHeight() {
        return drawingCanvasHeight;
    }

}


/**
 * 右键菜单类
 */
class ContextMenuPane extends ContextMenu {

    /**
     * 静态实例
     */
    private static ContextMenuPane instance = null;

    /**
     * 私有化构造器
     */
    private ContextMenuPane(BoardPane boardPane) {
        MenuItem editBtn = new MenuItem("编辑");
        editBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.editShapes();
        });

        MenuItem copyBtn = new MenuItem("复制");
        copyBtn.setOnAction(e -> {
            boardPane.copyChosedShapes();
        });

        MenuItem pasteBtn = new MenuItem("粘贴");
        pasteBtn.setOnAction(e -> {
            boardPane.pasteChosedShapes();
        });

        MenuItem deleteBtn = new MenuItem("删除");
        deleteBtn.setOnAction(e -> {
            boardPane.deleteChosedShapes();
        });
        MenuItem saveBtn = new MenuItem("保存");
        saveBtn.setOnAction(e -> {
            FileUtil.saveShape(boardPane.getStage(), boardPane.getSaveShapes());
        });
        MenuItem clearBtn = new MenuItem("清屏");
        clearBtn.setOnAction(e -> {
            boardPane.clear();
        });

        Menu alingnBtn = new Menu("对齐");
        MenuItem alingnTopBtn = new MenuItem("向上对齐");
        MenuItem alingnBottomBtn = new MenuItem("向下对齐");
        MenuItem alingnCenterBtn = new MenuItem("中间对齐");
        MenuItem alingnLetfBtn = new MenuItem("向左对齐");
        MenuItem alingnRightBtn = new MenuItem("向右对齐");
        alingnBtn.getItems().addAll(alingnTopBtn, alingnBottomBtn, alingnCenterBtn, alingnLetfBtn, alingnRightBtn);
        alingnTopBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.alignmentTopShapes();
        });
        alingnBottomBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.aligmentBottomShapes();
        });
        alingnCenterBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.aligmentCenterShapes();
        });
        alingnLetfBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.aligmentLeftShapes();
        });
        alingnRightBtn.setOnAction(e -> {
            if (boardPane.getAllChosedShapes().size() == 0) {
                NoChosedShapesAlert.alert();
                return;
            }
            boardPane.aligmentRightShapes();
        });

        getItems().add(editBtn);
        getItems().add(copyBtn);
        getItems().add(pasteBtn);
        getItems().add(deleteBtn);
        getItems().add(saveBtn);
        getItems().add(clearBtn);
        getItems().add(alingnBtn);
        //getItems().addAll(editBtn,clearBtn,pasteBtn,deleteBtn,saveBtn,clearBtn);
    }

    /**
     * 单例模式，获取唯一静态实例
     *
     * @param boardPane
     * @return
     */
    public static ContextMenuPane getInstance(BoardPane boardPane) {
        if (instance == null) {
            instance = new ContextMenuPane(boardPane);
        }
        return instance;
    }
}


/**
 * 未选择图形提示框类
 */
class NoChosedShapesAlert {
    public static void alert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.DECORATED);
        alert.setContentText("未选择图形！");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(PngPathEnum.WARNING.toString()));
        alert.showAndWait();
    }
}


/**
 * undo类
 */
class MementoCaretakerUndo {
    private Stack<Shape> undoStack = new Stack<>();

    public Shape getShapeWithPop() {
        return undoStack.pop();
    }

    public void setUndoStack(Shape shape) {
        undoStack.push(shape);
    }

    public Stack<Shape> getUndoStack() {
        return undoStack;
    }

    public void delUndoStack(Shape delshape) {
        Queue<Shape> queue = new LinkedList<>();
        while (!undoStack.empty()) {
            Shape shape = undoStack.pop();
            if (shape.equals(delshape)) {
                break;
            }
            queue.offer(shape);
        }
        while (!queue.isEmpty()) {
            undoStack.add(queue.poll());
        }
        //System.out.println("delshape: " + delshape+ " after del memntoStack" + memntoStack);
    }

}

/**
 * redo
 */
class MementoCaretakerRedo {

    private Stack<Shape> redoStack = new Stack<>();

    public Shape getShapeWithPop() {
        return redoStack.pop();
    }

    public void setRedoStack(Shape shape) {
        redoStack.push(shape);
    }

    public Stack<Shape> getUndoStack() {
        return redoStack;
    }

    public void delRedoStack(Shape delshape) {
        Queue<Shape> queue = new LinkedList<>();
        while (!redoStack.empty()) {
            Shape shape = redoStack.pop();
            if (shape.equals(delshape)) {
                break;
            }
            queue.offer(shape);
        }
        while (!queue.isEmpty()) {
            redoStack.add(queue.poll());
        }
        //System.out.println("delshape: " + delshape+ " after del memntoStack" + memntoStack);
    }

}