package view.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Address;
import model.Patient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class AddingDialogView extends Stage implements EditingDialog<Patient>{
    Patient patient;
    Address address;

    AnchorPane anchorPane = new AnchorPane();
    GridPane gridPane = new GridPane();

    private TextField surnameField = new TextField();
    private TextField firstNameField = new TextField();
    private TextField secondNameField = new TextField();
    private TextField nameOfDoctorField = new TextField();
    private TextField conclusionField = new TextField();

    Label surnameLabel = new Label("Surname");
    Label firstNameLabel = new Label("First Name:");
    Label secondNameLabel = new Label("Second Name");
    Label addressLabel = new Label("Address:");
    Label birthDateLabel = new Label("Date of Birth:");
    Label lastVisitLabel = new Label("Last Visit:");
    Label nameOfDoctorLabel = new Label("Doctor: ");
    Label conclusionLabel = new Label("Conclusion:");

    Button okButton = new Button("OK");
    Button cancelButton = new Button("Cancel");
    Button buttonAddress = new Button("Set Address");

    DatePicker birthdatePicker = new DatePicker();
    Date birthdate = new Date();
    DatePicker dateOfVisitPicker = new DatePicker();
    Date dateOfVisit = new Date();

    ColumnConstraints
            columnConstraints = new ColumnConstraints(0,0,Double.MAX_VALUE),
            columnConstraints1 = new ColumnConstraints(0, 0, Double.MAX_VALUE);
    RowConstraints
            rowConstraints = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints1 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints2 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints3 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints4 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints5 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints6 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints7 = new RowConstraints(0, 0, Double.MAX_VALUE);

    public AddingDialogView(){
        patient = new Patient();
        address = new Address();
        Scene dialog = new Scene(anchorPane, 500, 500);
        dialog.getStylesheets().add("Theme.css");
        setResizable(false);
        setTitle("Add Person");
        initModality(Modality.WINDOW_MODAL);
        setScene(dialog);

        birthdatePicker.setValue(LocalDate.now());
        dateOfVisitPicker.setValue(LocalDate.now());


        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 50.0);
        AnchorPane.setLeftAnchor(okButton, 100.0);
        AnchorPane.setBottomAnchor(okButton, 10.0);
        AnchorPane.setRightAnchor(cancelButton, 100.0);
        AnchorPane.setBottomAnchor(cancelButton, 10.0);


        anchorPane.setStyle("-fx-background-color: #3a3a3a");
        gridPane.setStyle("-fx-background-color: #1d1d1d");

        buttonAddress.setOnAction(actionEvent ->  {
            AddressPicker addressPicker = new AddressPicker(this);
            addressPicker.showAndWait();
            address = addressPicker.getResult().get();
        });
        okButton.setMinSize(80, 20);
        cancelButton.setMinSize(80, 20);
        configureGridConstraints();
        anchorPane.getChildren().addAll(gridPane, okButton, cancelButton);
    }

    private void configureGridConstraints(){
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints1.setHgrow(Priority.ALWAYS);
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints1.setVgrow(Priority.ALWAYS);
        rowConstraints2.setVgrow(Priority.ALWAYS);
        rowConstraints3.setVgrow(Priority.ALWAYS);
        rowConstraints4.setVgrow(Priority.ALWAYS);
        rowConstraints5.setVgrow(Priority.ALWAYS);
        rowConstraints6.setVgrow(Priority.ALWAYS);
        rowConstraints7.setVgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        gridPane.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2, rowConstraints3, rowConstraints4
                , rowConstraints5, rowConstraints6, rowConstraints7);

        GridPane.setConstraints(surnameLabel, 0,0);
        GridPane.setConstraints(surnameField, 1, 0);
        GridPane.setConstraints(firstNameLabel, 0, 1);
        GridPane.setConstraints(firstNameField, 1, 1);
        GridPane.setConstraints(secondNameLabel, 0, 2);
        GridPane.setConstraints(secondNameField, 1, 2);
        GridPane.setConstraints(addressLabel, 0, 3);
        GridPane.setConstraints(buttonAddress, 1, 3);
        GridPane.setConstraints(birthDateLabel, 0, 4);
        GridPane.setConstraints(birthdatePicker, 1, 4);
        GridPane.setConstraints(lastVisitLabel, 0,5);
        GridPane.setConstraints(dateOfVisitPicker, 1, 5);
        GridPane.setConstraints(nameOfDoctorLabel, 0, 6);
        GridPane.setConstraints(nameOfDoctorField, 1, 6);
        GridPane.setConstraints(conclusionLabel, 0, 7);
        GridPane.setConstraints(conclusionField, 1, 7);
        gridPane.getChildren().addAll(surnameLabel, surnameField, firstNameLabel, firstNameField, secondNameLabel,
                secondNameField, addressLabel, buttonAddress, birthDateLabel, birthdatePicker, lastVisitLabel,
                dateOfVisitPicker, nameOfDoctorLabel, nameOfDoctorField, conclusionLabel, conclusionField);

        okButton.setOnAction(actionEvent -> handleOk());
        cancelButton.setOnAction(actionEvent -> handleCancel());
    }

    public void handleOk(){
        if(isInputValid()){
            patient.setSurname(surnameField.getText());
            patient.setFirstName(firstNameField.getText());
            patient.setSecondName(secondNameField.getText());
            patient.setAddress(address);
            patient.setBirthdate(toDate(birthdatePicker.getValue()));
            patient.setDateOfVisit(toDate(dateOfVisitPicker.getValue()));
            patient.setDocName(nameOfDoctorField.getText());
            patient.setConclusion(conclusionField.getText());
            close();
        }
    }

    public Date toDate(LocalDate localDate){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    public void handleCancel(){
        close();
    }

    public Optional<Patient> getResult(){
        return Optional.of(this.patient);
    }

     public boolean isInputValid(){
        String errorMessage = "";

        if (!isStringInputValue(surnameField.getText())) {
            errorMessage += "No valid surname!\n";
        }
        if (!isStringInputValue(firstNameField.getText())) {
            errorMessage += "No valid first name!\n";
        }
        if (!isStringInputValue(secondNameField.getText())) {
            errorMessage += "No valid second name!\n";
        }
        if (birthdatePicker.getValue() == null) {
            errorMessage += "No valid birthdate\n";
        }
        if (dateOfVisitPicker.getValue() == null) {
            errorMessage += "No valid date of last visi!\n";
        }
        if(!isStringInputValue(nameOfDoctorField.getText())) {
            errorMessage += "No valid name of doctor\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorAlertation(errorMessage, this);
            return false;
        }
    }

    private boolean isStringInputValue(String stringInput) {
        return stringInput != null || stringInput.length() != 0;
    }


}
