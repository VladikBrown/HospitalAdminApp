package view.alerts;

import javafx.scene.control.Alert;

public abstract class HttpAllerts {
    public static void basicHttpInfoAlert(String statusCode, String reasonPhrase) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(statusCode);
        alert.setContentText(statusCode + " " + reasonPhrase);
        alert.showAndWait();
    }
}
