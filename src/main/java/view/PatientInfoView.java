package view;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import model.Patient;
import presenter.InfoViewPresenter;

public class PatientInfoView extends GridPane implements InfoView<Patient> {
    InfoViewPresenter infoViewPresenter;
    Label addressLabel = new Label("Address:"), addressInput = new Label("-----");
    Label birthDateLabel = new Label("Date of Birth:"), birthDateInput = new Label("-----");
    Label lastVisitLabel = new Label("Last visit:"), lastVisitInput = new Label("-----");
    Label nameOfDoctorLabel = new Label("Doctor: "), nameOfDoctorInput = new Label("-----");
    Label conclusionLabel = new Label("Conclusion:"), conclusionInput = new Label("-----");

    ColumnConstraints
            columnConstraints = new ColumnConstraints(0, 0, Double.MAX_VALUE),
            columnConstraints1 = new ColumnConstraints(0, 0, Double.MAX_VALUE);
    RowConstraints
            rowConstraints = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints1 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints2 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints3 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints4 = new RowConstraints(0, 0, Double.MAX_VALUE);

    public PatientInfoView() {
        configureGridPaneConstraints();
    }

    public PatientInfoView(InfoViewPresenter infoViewPresenter) {
        this.infoViewPresenter = infoViewPresenter;
        configureGridPaneConstraints();
    }

    public void configureGridPaneConstraints() {
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints1.setHgrow(Priority.ALWAYS);
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints1.setVgrow(Priority.ALWAYS);
        rowConstraints2.setVgrow(Priority.ALWAYS);
        rowConstraints3.setVgrow(Priority.ALWAYS);
        rowConstraints4.setVgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        this.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2, rowConstraints3, rowConstraints4);

        GridPane.setConstraints(addressLabel, 0,0);
        GridPane.setConstraints(addressInput, 1, 0);
        GridPane.setConstraints(birthDateLabel, 0, 1);
        GridPane.setConstraints(birthDateInput, 1, 1);
        GridPane.setConstraints(lastVisitLabel, 0, 2);
        GridPane.setConstraints(lastVisitInput, 1, 2);
        GridPane.setConstraints(nameOfDoctorLabel, 0, 3);
        GridPane.setConstraints(nameOfDoctorInput, 1, 3);
        GridPane.setConstraints(conclusionLabel, 0, 4);
        GridPane.setConstraints(conclusionInput, 1, 4);
        this.getChildren().addAll(addressLabel, addressInput, birthDateLabel, birthDateInput, lastVisitLabel,
                lastVisitInput, nameOfDoctorLabel, nameOfDoctorInput, conclusionLabel, conclusionInput);

        this.setGridLinesVisible(true);
    }

    @Override
    public void showInfo(Patient patient) {
        if (patient != null) {
            addressInput.setText(patient.getAddress().toString());
            birthDateInput.setText(patient.getBirthdate().toString());
            lastVisitInput.setText(patient.getDateOfVisit().toString());
            nameOfDoctorInput.setText(patient.getDocName());
            conclusionInput.setText(patient.getConclusion());
        } else {
            addressInput.setText("");
            birthDateInput.setText("");
            lastVisitInput.setText("");
            nameOfDoctorInput.setText("");
            conclusionInput.setText("");
        }
    }

    @Override
    public InfoViewPresenter getInfoViewPresenter() {
        return this.infoViewPresenter;
    }

    @Override
    public void setInfoViewPresenter(InfoViewPresenter infoViewPresenter) {
        this.infoViewPresenter = infoViewPresenter;
    }
}
