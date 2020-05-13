package presenter;

import javafx.collections.ObservableList;
import model.IModel;
import model.PatientModel;
import model.entity.Patient;
import org.bson.Document;
import view.*;
import view.alerts.PatientAlerts;
import view.dialogs.AddPatientDialog;
import view.dialogs.FindPatientDialog;

import java.util.Optional;

public class PatientPresenter implements InfoViewPresenter<Patient>, TablePagerViewPresenter<Patient>, ToolBarViewPresenter {
    private Patient selectedPatient;
    private final TablePagerView<Patient> patientTablePagerView;
    private InfoView<Patient> patientInfoView;
    private final IToolBarView toolBarView;
    private ITableView currentPage;
    private IModel<Patient> model;

    public PatientPresenter(PatientTablePagerView tpv, PatientInfoView piv, ToolBarView tbv) {
        patientTablePagerView = tpv;
        patientInfoView = piv;
        toolBarView = tbv;
        this.model = new PatientModel(this);
    }

    public void onFind() {
        var patientDialog = new FindPatientDialog();
        patientDialog.show();
        patientDialog.getResult().ifPresent(document -> model.find(document));
    }

    @Override
    public void onAmountOfCellsChanged() {
        onUpdate();
    }

    @Override
    public void showTable(ObservableList<Patient> patients) {
        if (patients.size() > 0) {
            patientTablePagerView.createPager(patients);
        } else {
            PatientAlerts.displayError();
        }
    }

    @Override
    public void setOnItemSelected(Patient selectedItem) {
        showSelected(selectedItem);
    }

    @Override
    public void onUpdate() {
        patientTablePagerView.createPager(model.getAll());
    }

    @Override
    public void onAdd() {
        var patientDialog = new AddPatientDialog();
        patientDialog.show();
        patientDialog.getResult().ifPresent(patient -> model.add(patient));
    }

    @Override
    public void onDelete() {
        if (selectedPatient != null) {
            model.delete(selectedPatient);
            ((PatientTableView) currentPage).getItems().removeAll(selectedPatient);
            selectedPatient = null;
        } else {
            PatientAlerts.notSelectedError();
        }
    }

    @Override
    public InfoView<Patient> getInfoView() {
        return patientInfoView;
    }

    @Override
    public void setInfoView(InfoView<Patient> infoView) {
        this.patientInfoView = infoView;
    }

    @Override
    public void showSelected(Patient patient) {
        this.selectedPatient = patient;
        patientInfoView.showInfo(patient);
    }

    public Patient getSelectedItem() {
        return selectedPatient;
    }

    @Override
    public void setCurrentPage(PatientTableView patientTableView) {
        this.currentPage = patientTableView;
    }

    @Override
    public boolean updateCache(int offset, int limit) {
        return false;
    }

    @Override
    public void onFindAndDelete() {
        var patientDialog = new FindPatientDialog();
        patientDialog.show();
        Optional<Document> documentOptional = patientDialog.getResult();
        if (documentOptional.isPresent()) {
            int amountOfPatients = model.deleteAll(documentOptional.get());
            PatientAlerts.deletingInfo(amountOfPatients);
        }
        onUpdate();
    }

    @Override
    public IModel getModel() {
        return this.model;
    }

    @Override
    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public void loadView() {
        patientInfoView.setInfoViewPresenter(this);
        patientTablePagerView.setTablePagerViewPresenter(this);
        toolBarView.setToolBarViewPresenter(this);
    }
}
