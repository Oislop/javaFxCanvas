package drawing.view.stage.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 对话框
 */
public class AlertBuilder {
    private final Alert alert;
    private Alert.AlertType alertType;
    private String title;
    private String headerText = null;
    private StageStyle stageStyle;
    private String contentText;
    private Stage stage;
    private String iconPath;
    private ArrayList<ButtonType> buttonTypeArrayList = new ArrayList<>();
    private Optional<ButtonType> buttonTypeResult;
    private Dialog<Pair<String, String>> dialog = null;

    public Alert getAlert() {
        return alert;
    }

    private AlertBuilder(builder build) {
        alert = build.alert;
        alertType = build.alertType;
        title = build.title;
        headerText = build.headerText;
        stageStyle = build.stageStyle;
        contentText = build.contentText;
        stage = build.stage;
        iconPath = build.iconPath;
        if(build.dialog != null){
            dialog = build.dialog;
        }
        if(build.buttonTypeArrayList.size() > 0){
            buttonTypeArrayList = build.buttonTypeArrayList;
        }

        alert();
    }

    /**
     * 内部builder类
     */
    public static class builder {
        private final Alert alert;
        private Alert.AlertType alertType;
        private String title;
        private String headerText = null;
        private StageStyle stageStyle;
        private String contentText;
        private Stage stage;
        private String iconPath;
        private Dialog<Pair<String, String>> dialog = null;
        private ArrayList<ButtonType> buttonTypeArrayList = new ArrayList<>();

        public AlertBuilder build() {
            return new AlertBuilder(this);
        }

        public builder(Alert alert) {
            this.alert = alert;
            this.alertType = alert.getAlertType();
        }


        public builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public builder setHeaderText(String headerText) {
            this.headerText = headerText;
            return this;
        }

        public builder setStageStyle(StageStyle stageStyle) {
            this.stageStyle = stageStyle;
            return this;
        }

        public builder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public builder setStage() {
            this.stage = (Stage) alert.getDialogPane().getScene().getWindow();
            return this;
        }

        public builder setIconPath(String iconPath) {
            this.iconPath = iconPath;
            return this;
        }

        public builder setButtonTypes(ArrayList<ButtonType> buttonTypeArrayList){
            this.buttonTypeArrayList = buttonTypeArrayList;
            return this;
        }

        public builder setDialog(Dialog<Pair<String, String>> dialog){
            this.dialog = dialog;
            return this;
        }


    }


    private void alert() {
        alert.setTitle(title);
        alert.setAlertType(alertType);
        alert.setHeaderText(headerText);
        alert.initStyle(stageStyle);
        alert.setContentText(contentText);
        stage.getIcons().add(new Image(iconPath));
        if(buttonTypeArrayList.size() > 0){
            alert.getButtonTypes().setAll(buttonTypeArrayList);
            buttonTypeResult = alert.showAndWait();
        } else {
            alert.showAndWait();
        }
    }

    public Optional<ButtonType> getButtonTypeResult() {
        return buttonTypeResult;
    }

}
