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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public abstract class PatientEditingDialog<T> implements EditingDialog<T> {
    private static final int HEIGHT = 500;
    private static final int WIDTH = 500;
    protected Stage stage;
    protected Address address;
    protected TextField surnameField = new TextField();
    protected TextField firstNameField = new TextField();
    protected TextField secondNameField = new TextField();
    protected TextField nameOfDoctorField = new TextField();
    protected TextField conclusionField = new TextField();
    protected DatePicker birthdatePicker = new DatePicker();
    protected DatePicker dateOfVisitPicker = new DatePicker();
    AnchorPane anchorPane = new AnchorPane();
    GridPane gridPane = new GridPane();
    Button okButton = new Button("OK");
    Button cancelButton = new Button("Cancel");
    Button buttonAddress = new Button("Set Address");
    ColumnConstraints
            columnConstraints = new ColumnConstraints(0, 0, Double.MAX_VALUE),
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
    private Label surnameLabel = new Label("Surname");
    private Label firstNameLabel = new Label("First Name:");
    private Label secondNameLabel = new Label("Second Name");
    private Label addressLabel = new Label("Address:");
    private Label birthDateLabel = new Label("Date of Birth:");
    private Label lastVisitLabel = new Label("Last Visit:");
    private Label nameOfDoctorLabel = new Label("Doctor: ");
    private Label conclusionLabel = new Label("Conclusion:");

    PatientEditingDialog() {
        stage = new Stage();
        Scene dialog = new Scene(anchorPane, HEIGHT, WIDTH);
        dialog.getStylesheets().add("Theme.css");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(dialog);

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


        okButton.setMinSize(80, 20);
        cancelButton.setMinSize(80, 20);
        configureGridConstraints();
        anchorPane.getChildren().addAll(gridPane, okButton, cancelButton);
    }

    private void configureGridConstraints() {
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

        GridPane.setConstraints(surnameLabel, 0, 0);
        GridPane.setConstraints(surnameField, 1, 0);
        GridPane.setConstraints(firstNameLabel, 0, 1);
        GridPane.setConstraints(firstNameField, 1, 1);
        GridPane.setConstraints(secondNameLabel, 0, 2);
        GridPane.setConstraints(secondNameField, 1, 2);
        GridPane.setConstraints(addressLabel, 0, 3);
        GridPane.setConstraints(buttonAddress, 1, 3);
        GridPane.setConstraints(birthDateLabel, 0, 4);
        GridPane.setConstraints(birthdatePicker, 1, 4);
        GridPane.setConstraints(lastVisitLabel, 0, 5);
        GridPane.setConstraints(dateOfVisitPicker, 1, 5);
        GridPane.setConstraints(nameOfDoctorLabel, 0, 6);
        GridPane.setConstraints(nameOfDoctorField, 1, 6);
        GridPane.setConstraints(conclusionLabel, 0, 7);
        GridPane.setConstraints(conclusionField, 1, 7);
        gridPane.getChildren().addAll(surnameLabel, surnameField, firstNameLabel, firstNameField, secondNameLabel,
                secondNameField, addressLabel, buttonAddress, birthDateLabel, birthdatePicker, lastVisitLabel,
                dateOfVisitPicker, nameOfDoctorLabel, nameOfDoctorField, conclusionLabel, conclusionField);
    }

    public Date toDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    abstract void handleOk();

    abstract void handleCancel();

    abstract void handleAddressButton();


    public void show() {
        buttonAddress.setOnAction(actionEvent -> handleAddressButton());
        okButton.setOnAction(actionEvent -> handleOk());
        cancelButton.setOnAction(actionEvent -> handleCancel());
        stage.showAndWait();
    }

    public boolean isStringInputValid(String stringInput) {
        return stringInput != null && stringInput.length() != 0;
    }
}

