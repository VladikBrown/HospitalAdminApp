package view.dialogs;

import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.Optional;

public interface EditingDialog<Result> {
    default void errorAlertation(String errorMessage, Window owner) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    boolean isInputValid();

    Optional<Result> getResult();
}
