package drawing.config;

public enum PngPathEnum {
    /**
     * 图标
     */
    LOGO("/resource/icon.jpg"),
    /**
     * 笔
     */
    PEN("/resource/pen.png"),
    /**
     * 橡皮
     */
    RUBBER("/resource/rubber.png"),
    /**
     * 涂漆
     */
    BARREL("/resource/barrel.png"),
    /**
     * 文本
     */
    TEXT("/resource/text.png"),
    /**
     * 直线
     */
    LINE("/resource/line.png"),
    /**
     * /圆角矩形
     */
    RECTANGLEY("/resource/squareY.png"),
    /**
     * 直角矩形
     */
    RECTANGLEZ("/resource/square.png"),
    /**
     * 椭圆
     */
    OVAL("/resource/oval.png"),

    /**
     * 鼠标
     */
    MOUSE("/resource/mouse.png"),

    /**
     * 多边形
     */
    POLYGON("/resource/polygon.png"),

    /**
     * 自定义多边形
     */
    POLYGON1("/resource/polygondraw.png"),
    /**
     * 警告
     */
    WARNING("/resource/waring.png"),
    /**
     * 关于
     */
    ABOUT("/resource/about.png"),
    /**
     * 编辑
     *
     */
    EDIT("/resource/edit.png"),
    /**
     * 显示shape
     */
    SHOW("/resource/show.png"),
    /**
     * 隐藏shape
     */
    HIDE("/resource/hide.png"),

    UP("/resource/up.png"),
    /**
     * 拾色器
     */
    COLORPICKER("/resource/colorpicker.png");

    String path;

    PngPathEnum(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
