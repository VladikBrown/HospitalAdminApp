package presenter.impl;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.CacheableModel;
import model.IModel;
import model.entity.Patient;
import org.bson.Document;
import presenter.InfoViewPresenter;
import presenter.TablePagerViewPresenter;
import presenter.ToolBarViewPresenter;
import view.alerts.PatientAlerts;
import view.components.ITableView;
import view.components.IToolBarView;
import view.components.InfoView;
import view.components.TablePagerView;
import view.components.impl.PatientInfoView;
import view.components.impl.PatientTablePagerView;
import view.components.impl.PatientTableView;
import view.components.impl.ToolBarView;
import view.dialogs.AddPatientDialog;
import view.dialogs.FindPatientDialog;

import java.util.Optional;

@Deprecated
public class PatientPresenter implements InfoViewPresenter<Patient>, TablePagerViewPresenter<Patient>, ToolBarViewPresenter {

    private Patient selectedPatient;
    private final TablePagerView<Patient> patientTablePagerView;
    private final IToolBarView toolBarView;
    private InfoView<Patient> patientInfoView;
    private ITableView currentPage;
    private CacheableModel<Patient> model;
    //CollectionChangerView
    private boolean isChangeCollectionActive = false;

    public PatientPresenter(PatientTablePagerView tpv, PatientInfoView piv, ToolBarView tbv) {
        patientTablePagerView = tpv;
        patientInfoView = piv;
        toolBarView = tbv;
        //this.model = new PatientModel(this);
    }

    @Override
    public void onAmountOfCellsChanged() {
        onUpdate();
    }

    @Override
    public void onFind() {
        var patientDialog = new FindPatientDialog();
        patientDialog.show();
        patientDialog.getResult().ifPresent(document -> model.get(document, 0, patientTablePagerView.getRowsPerPage()));
        showTable((ObservableList<Patient>) model.getCache(), model.getCurrentTotalNumberOfRecords());
    }

    @Override
    public void showTable(ObservableList<Patient> items, int totalAmountOfPatients) {
        if (items.size() > 0) {

            patientTablePagerView.createPager(items, totalAmountOfPatients);
        } else {
            PatientAlerts.displayingError();
        }
    }


    @Override
    public void setOnItemSelected(Patient selectedItem) {
        showSelected(selectedItem);
    }

    @Override
    public void setSelectedItem(Patient item) {

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
    public void onUpdate() {
        showTable(model.getAll(), model.getCurrentTotalNumberOfRecords());
    }

    @Override
    public int getRowsPerPage() {
        return 0;
    }

    @Override
    public void setRowsPerPage(int rowsPerPage) {

    }

    @Override
    public TableView<Patient> getCurrentPage() {
        return null;
    }

    @Override
    public void setCurrentPage(ITableView patientTableView) {
        this.currentPage = patientTableView;
    }

    @Override
    public boolean updateCache(int offset, int limit) {
        model.get(Document.parse(model.getCurrentState()), offset, limit);
        //TODO wasCacheUpdated
        return true;
    }

    @Override
    public void onFindAndDelete() {
        var patientDialog = new FindPatientDialog();
        patientDialog.show();
        Optional<Document> documentOptional = patientDialog.getResult();
        if (documentOptional.isPresent()) {
            int amountOfPatients = model.deleteAll(documentOptional.get());
            if (amountOfPatients > 0) {
                PatientAlerts.deletingInfo(amountOfPatients);
            }
        }
        onUpdate();
    }

    @Override
    public void onChangeCollection() {
        if (!isChangeCollectionActive) {

//            ((ToolBarView) toolBarView).getItems().add(menu);
        } else {
            //          ((PatientModel) model).reconnect(inputCollectionField.getText());
            //        ((ToolBarView) toolBarView).getItems().remove(menu);
        }
        isChangeCollectionActive = !isChangeCollectionActive;
    }

    @Override
    public CacheableModel<Patient> getModel() {
        return this.model;
    }

    @Override
    public void setModel(IModel model) {
        if (model instanceof CacheableModel) {
            this.model = ((CacheableModel<Patient>) model);
        }
    }

    @Override
    public void loadView() {
        patientInfoView.setInfoViewPresenter(this);
        patientTablePagerView.setTablePagerViewPresenter(this);
        toolBarView.setToolBarViewPresenter(this);
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public TablePagerView<Patient> getPatientTablePagerView() {
        return patientTablePagerView;
    }

    public InfoView<Patient> getPatientInfoView() {
        return patientInfoView;
    }

    public IToolBarView getToolBarView() {
        return toolBarView;
    }
}
