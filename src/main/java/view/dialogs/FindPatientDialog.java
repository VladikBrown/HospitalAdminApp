package view.dialogs;

import model.Address;
import model.FieldsDB;
import org.bson.Document;

import java.util.Optional;

public class FindPatientDialog extends PatientEditingDialog<Document> {
    private Document document;

    public FindPatientDialog() {
        super();
    }

    private void initDocument() {
        appendSurname();
        appendFirstName();
        appendSecondName();
        appendAddressStreet();
        appendAddressHome();
        appendAddressBuilding();
        appendAddressApartment();
        appendDoctorsName();
        appendConclusion();
    }

    private void appendSurname() {
        if (isStringInputValid(surnameField.getText())) {
            document.append(FieldsDB.SURNAME, surnameField.getText());
        }
    }

    private void appendFirstName() {
        if (isStringInputValid(firstNameField.getText())) {
            document.append(FieldsDB.FIRST_NAME, firstNameField.getText());
        }
    }

    private void appendSecondName() {
        if (isStringInputValid(secondNameField.getText())) {
            document.append(FieldsDB.SECOND_NAME, secondNameField.getText());
        }
    }

    //find(eq(address.street, "Minsk"));
    private void appendAddressStreet() {
        if (address != null && address.getStreet() != null) {
            document.append(FieldsDB.ADDRESS + "." + FieldsDB.STREET, address.getStreet());
        }
    }

    private void appendAddressHome() {
        if (address != null && address.getHomeNumber() != null) {
            document.append(FieldsDB.ADDRESS + "." + FieldsDB.HOME_NUMBER, address.getHomeNumber());
        }
    }

    private void appendAddressBuilding() {
        if (address != null && address.getBuildingNumber() != null) {
            document.append(FieldsDB.ADDRESS + "." + FieldsDB.BUILDING_NUMBER, address.getBuildingNumber());
        }
    }

    private void appendAddressApartment() {
        if (address != null && address.getApartmentsNumber() != null) {
            document.append(FieldsDB.ADDRESS + "." + FieldsDB.APARTMENTS_NUMBER, address.getApartmentsNumber());
        }
    }

    private void appendDoctorsName() {
        if (isStringInputValid(nameOfDoctorField.getText())) {
            document.append(FieldsDB.NAME_OF_DOCTOR, nameOfDoctorField.getText());
        }
    }

    private void appendConclusion() {
        if (isStringInputValid(conclusionField.getText())) {
            document.append(FieldsDB.CONCLUSION, conclusionField.getText());
        }
    }

    @Override
    void handleAddressButton() {
        AddressPicker addressPicker = new AddressPicker(stage);
        addressPicker.showFindDialog();
        Optional<Address> optionalAddress = addressPicker.getResult();
        optionalAddress.ifPresentOrElse(value -> address = value, () -> address = new Address());
    }

    @Override
    void handleOk() {
        document = new Document();
        initDocument();
        super.stage.close();
    }

    @Override
    void handleCancel() {
        super.stage.close();
    }

    @Override
    public boolean isInputValid() {
        return false;
    }

    @Override
    public Optional<Document> getResult() {
        return Optional.ofNullable(document);
    }
}
