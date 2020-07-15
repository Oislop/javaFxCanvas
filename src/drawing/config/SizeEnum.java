package drawing.config;

public enum SizeEnum {
    TOOLBAR_BUTTON_WIDTH(35),
    TOOLBAR_BUTTON_HEIGHT(35),
    COLOR_BUTTON_WIDTH(45),
    COLOR_BUTTON_HEIGHT(45),
    DETAIL_WIDTH(90),
    COLOR_PICKER_WIDTH(90),
    CANVAS_WIDTH(911),
    CANVAS_HEIGHT(814),
    STAGE_WIDTH(1648),
    STAGE_HEIGHT(973);
    int value;

    SizeEnum(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
