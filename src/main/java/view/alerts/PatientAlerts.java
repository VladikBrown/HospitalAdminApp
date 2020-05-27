package view.alerts;

import javafx.scene.control.Alert;

public abstract class PatientAlerts {
    public static void displayingError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Failed to display!");
        alert.setContentText("Please, try to enter correct info");
        alert.showAndWait();
    }

    public static void notSelectedError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("You've not selected any patient!");
        alert.setContentText("Please, select patient and try again");
        alert.showAndWait();
    }

    public static void deletingInfo(int numberOfDeleted) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Deleting");
        alert.setContentText("You've deleted " + numberOfDeleted + " patients");
        alert.showAndWait();
    }
}
