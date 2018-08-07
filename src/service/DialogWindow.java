package service;

import javafx.scene.control.Alert;

public class DialogWindow {
    public DialogWindow(Alert.AlertType AlertType, String title, String header, String content){
        Alert a = new Alert(AlertType);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

}
