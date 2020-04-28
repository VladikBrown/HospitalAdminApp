package presenter;

import javafx.scene.control.Alert;
import model.Model;
import model.Patient;
import view.PatientInfoView;
import view.PatientTablePagerView;
import view.PatientTableView;
import view.ToolBarView;
import view.dialogs.AddingDialogView;

public class Presenter implements IPresenter {
    private Patient selectedPatient;
    private PatientTablePagerView patientTablePagerView;
    private PatientInfoView patientInfoView;
    private ToolBarView toolBarView;
    private Model model;
    private PatientTableView currentPage;

    public Presenter(PatientTablePagerView ptv, PatientInfoView piv, ToolBarView tbv){
        patientTablePagerView = ptv;
        patientInfoView = piv;
        toolBarView = tbv;
        this.model = new Model(this);
    }

    public void setOnFindButton(){
        
    }
    
    public void setOnUpdateButton() {
        patientTablePagerView.createPager(model.getAllPatients());
    }

    public void setOnAdd(){
        AddingDialogView addingDialogView = new AddingDialogView();
        addingDialogView.showAndWait();
        Patient patient = addingDialogView.getResult().get();
        model.addPatient(patient);
    }

    public void setOnDelete(){
        if(selectedPatient != null){
            model.deletePatient(selectedPatient);
            currentPage.getItems().removeAll(selectedPatient);
            selectedPatient = null;
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("You've not selected any patient!");
            alert.setContentText("Please, select patient and try again");
            alert.showAndWait();
        }
    }

    public void setOnPatientSelected(Patient patient){
        this.selectedPatient = patient;
        patientInfoView.showPatientsInfo(patient);
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setCurrentPage(PatientTableView patientTableView) {
        this.currentPage = patientTableView;
    }
}
