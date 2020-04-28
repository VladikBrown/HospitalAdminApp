package view.dialogs;

import javafx.stage.Stage;
import model.Patient;

import java.util.Optional;

public class EditPatientDialog extends Stage implements EditingDialog<Patient> {





     @Override
    public void handleOk() {

    }

    @Override
    public boolean isInputValid() {
        return false;
    }

    @Override
    public Optional<Patient> getResult() {
        return Optional.empty();
    }
}
