package view.dialogs;

import model.Address;
import model.Patient;

import java.util.Optional;

public class AddPatientDialog extends PatientEditingDialog<Patient> {
    protected Address address;
    private Patient patient;

    public AddPatientDialog() {
        super();
    }

    @Override
    public void handleOk() {
        if (isInputValid()) {
            initPatient();
        }
    }

    @Override
    public void handleCancel() {
        super.stage.close();
    }

    @Override
    public Optional<Patient> getResult() {
        return Optional.ofNullable(this.patient);
    }

    private void initPatient() {
        patient = new Patient();
        patient.setSurname(surnameField.getText());
        patient.setFirstName(firstNameField.getText());
        patient.setSecondName(secondNameField.getText());
        patient.setAddress(address);
        patient.setBirthdate(toDate(birthdatePicker.getValue()));
        patient.setDateOfVisit(toDate(dateOfVisitPicker.getValue()));
        patient.setDocName(nameOfDoctorField.getText());
        patient.setConclusion(conclusionField.getText());
        super.stage.close();
    }

    public boolean isInputValid() {
        String errorMessage = "";
        //TODO: сделать свитч вместо ифов???

        if (!isStringInputValid(surnameField.getText())) {
            errorMessage += "No valid surname!\n";
        }
        if (!isStringInputValid(firstNameField.getText())) {
            errorMessage += "No valid first name!\n";
        }
        if (!isStringInputValid(secondNameField.getText())) {
            errorMessage += "No valid second name!\n";
        }
        if (birthdatePicker.getValue() == null) {
            errorMessage += "No valid birthdate\n";
        }
        if (dateOfVisitPicker.getValue() == null) {
            errorMessage += "No valid date of last visit!\n";
        }
        if (!isStringInputValid(nameOfDoctorField.getText())) {
            errorMessage += "No valid name of doctor\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorAlertation(errorMessage, super.stage);
            return false;
        }
    }

    @Override
    void handleAddressButton() {
        AddressPicker addressPicker = new AddressPicker(super.stage);
        addressPicker.showAddDialog();
        Optional<Address> optionalAddress = addressPicker.getResult();
        optionalAddress.ifPresent(value -> address = value);
    }
}
