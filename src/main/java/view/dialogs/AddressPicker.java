package view.dialogs;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Address;

import java.util.Optional;

public class AddressPicker extends Stage implements EditingDialog<Address> {
    private Address address;
    private AnchorPane anchorPane = new AnchorPane();
    private VBox flowPane = new VBox();
    private TextField streetField = new TextField("street");
    private TextField homeNumberField = new TextField("home");
    private TextField buildingNumberField = new TextField("building");
    private TextField apartmentsNumberField = new TextField("apartment");
    private Button buttonAddressOk = new Button("OK");

    AddressPicker(Stage owner){
        address = new Address();
        Scene addressScene = new Scene(anchorPane);
        addressScene.getStylesheets().add("Theme.css");
        this.initOwner(owner);
        this.setScene(addressScene);
        AnchorPane.setTopAnchor(flowPane, 0.0);
        AnchorPane.setBottomAnchor(flowPane, 0.0);
        AnchorPane.setRightAnchor(flowPane, 0.0);
        AnchorPane.setLeftAnchor(flowPane, 0.0);
        buttonAddressOk.setPrefWidth(230);
        setResizable(false);
        flowPane.getChildren().addAll(streetField, homeNumberField, buildingNumberField, apartmentsNumberField, buttonAddressOk);
        anchorPane.getChildren().addAll(flowPane);
        buttonAddressOk.setOnAction(actionEvent -> handleOk());
    }

    @Override
    public void handleOk() {
        if(isInputValid()){
            address.setStreet(streetField.getText());
            address.setHomeNumber(Integer.parseInt(homeNumberField.getText()));
            address.setBuildingNumber(Integer.parseInt(buildingNumberField.getText()));
            address.setApartmentsNumber(Integer.parseInt(apartmentsNumberField.getText()));
            close();
        }
    }

    @Override
    public boolean isInputValid() {
        String errorMessage = "";

        if (!isStreetInputValid(streetField.getText())) {
            errorMessage += "No valid street!\n";
        }
        if (!isHomeInputValid(homeNumberField.getText())) {
            errorMessage += "No valid home number\n";
        }
        if (!isBuildingInputValid(buildingNumberField.getText())) {
                errorMessage += "No valid building number\n";
        }
        if (!isApartmentInputValid(apartmentsNumberField.getText())) {
            errorMessage += "No valid apartments number\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorAlertation(errorMessage, this);
            return false;
        }
    }

    private boolean isStreetInputValid(String streetInput){
        return streetInput != null && !streetInput.equals("street") && streetInput.length() != 0;
    }

    private boolean isHomeInputValid(String homeInput){
        return homeInput != null && !homeInput.equals("home") && isNumericValue(homeInput);
    }

    private boolean isBuildingInputValid(String buildingInput){
        return buildingInput != null && !buildingInput.equals("building") && isNumericValue(buildingInput);
    }

    private boolean isApartmentInputValid(String apartmentInput){
        return apartmentInput != null && !apartmentInput.equals("apartment") && isNumericValue(apartmentInput);
    }


    private boolean isNumericValue(String string){
        try {
            Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public Optional<Address> getResult() {
        return Optional.of(this.address);
    }
}
