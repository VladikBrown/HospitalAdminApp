package view.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.entity.Address;

import java.util.Optional;

class AddressPicker extends Stage implements EditingDialog {
    private Address address;
    private AnchorPane anchorPane = new AnchorPane();
    private VBox flowPane = new VBox();
    private ColumnConstraints
            columnConstraints = new ColumnConstraints(0, 0, Double.MAX_VALUE),
            columnConstraints1 = new ColumnConstraints(0, 0, Double.MAX_VALUE);
    private RowConstraints
            rowConstraints = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints1 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints2 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints3 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints4 = new RowConstraints(0, 0, Double.MAX_VALUE);
    private GridPane gridPane = new GridPane();
    private Label streetLabel = new Label("Street: ");
    private Label homeLabel = new Label("Home: ");
    private Label buildingLabel = new Label("Building: ");
    private Label apartmentLabel = new Label("Ap: ");
    private TextField streetField = new TextField();
    private TextField homeNumberField = new TextField();
    private Button buttonAddressOk = new Button("OK");
    private TextField buildingNumberField = new TextField();
    private TextField apartmentsNumberField = new TextField();
    private Button buttonAddressCancel = new Button("Cancel");

    AddressPicker(Stage owner) {
        address = new Address();
        Scene addressScene = new Scene(anchorPane, 500, 300);
        addressScene.getStylesheets().add("Theme.css");
        this.initOwner(owner);
        this.setScene(addressScene);

        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 50.0);
        AnchorPane.setLeftAnchor(buttonAddressOk, 100.0);
        AnchorPane.setBottomAnchor(buttonAddressOk, 10.0);
        AnchorPane.setRightAnchor(buttonAddressCancel, 100.0);
        AnchorPane.setBottomAnchor(buttonAddressCancel, 10.0);

        anchorPane.setStyle("-fx-background-color: #3a3a3a");
        gridPane.setStyle("-fx-background-color: #1d1d1d");

        setResizable(false);
        flowPane.getChildren().addAll(streetField, homeNumberField, buildingNumberField, apartmentsNumberField, buttonAddressOk);
        configureGridConstraints();
        anchorPane.getChildren().addAll(gridPane, buttonAddressOk, buttonAddressCancel);

        buttonAddressCancel.setOnAction(actionEvent -> close());
    }

    private void configureGridConstraints() {
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints1.setHgrow(Priority.ALWAYS);

        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints1.setVgrow(Priority.ALWAYS);
        rowConstraints2.setVgrow(Priority.ALWAYS);
        rowConstraints3.setVgrow(Priority.ALWAYS);
        rowConstraints4.setVgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        gridPane.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2, rowConstraints3, rowConstraints4);

        GridPane.setConstraints(streetLabel, 0, 0);
        GridPane.setConstraints(streetField, 1, 0);
        GridPane.setConstraints(homeLabel, 0, 1);
        GridPane.setConstraints(homeNumberField, 1, 1);
        GridPane.setConstraints(buildingLabel, 0, 2);
        GridPane.setConstraints(buildingNumberField, 1, 2);
        GridPane.setConstraints(apartmentLabel, 0, 3);
        GridPane.setConstraints(apartmentsNumberField, 1, 3);

        gridPane.getChildren().addAll(streetLabel, streetField, homeLabel, homeNumberField, buildingLabel, buildingNumberField,
                apartmentLabel, apartmentsNumberField);
    }

    public void handleAddOk() {
        if (isInputValid()) {
            address.setStreet(streetField.getText());
            address.setHomeNumber(Integer.parseInt(homeNumberField.getText()));
            address.setBuildingNumber(Integer.parseInt(buildingNumberField.getText()));
            address.setApartmentsNumber(Integer.parseInt(apartmentsNumberField.getText()));
            close();
        }
    }

    public void handleFindOk() {
        if (isStringInputValid(streetField.getText())) {
            address.setStreet(streetField.getText());
        } else address.setStreet(null);
        if (isNumberInputValid(homeNumberField.getText())) {
            address.setHomeNumber(Integer.parseInt(homeNumberField.getText()));
        } else address.setHomeNumber(null);
        if (isNumberInputValid(buildingNumberField.getText())) {
            address.setBuildingNumber(Integer.parseInt(buildingNumberField.getText()));
        } else address.setBuildingNumber(null);
        if (isNumberInputValid(apartmentsNumberField.getText())) {
            address.setApartmentsNumber(Integer.parseInt(apartmentsNumberField.getText()));
        } else address.setApartmentsNumber(null);
        close();
    }

    @Override
    public boolean isInputValid() {
        String errorMessage = "";

        if (!isStringInputValid(streetField.getText())) {
            errorMessage += "No valid street!\n";
        }
        if (!isNumberInputValid(homeNumberField.getText())) {
            errorMessage += "No valid home number\n";
        }
        if (!isNumberInputValid(buildingNumberField.getText())) {
            errorMessage += "No valid building number\n";
        }
        if (!isNumberInputValid(apartmentsNumberField.getText())) {
            errorMessage += "No valid apartments number\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorAlertation(errorMessage, this);
            return false;
        }
    }

    private boolean isStringInputValid(String streetInput) {
        return streetInput != null && streetInput.length() != 0;
    }

    private boolean isNumberInputValid(String homeInput) {
        return homeInput != null && isNumericValue(homeInput);
    }

    private boolean isNumericValue(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void showAddDialog() {
        buttonAddressOk.setOnAction(actionEvent -> handleAddOk());
        showAndWait();
    }

    public void showFindDialog() {
        buttonAddressOk.setOnAction(actionEvent -> handleFindOk());
        showAndWait();
    }


    public Optional<Address> getResult() {
        return Optional.of(this.address);
    }
}
