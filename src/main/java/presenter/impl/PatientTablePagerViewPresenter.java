package presenter.impl;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.CacheableModel;
import model.IModel;
import model.entity.Patient;
import org.bson.Document;
import presenter.TablePagerViewPresenter;
import presenter.mediator.PatientPresentersMediator;
import view.alerts.PatientAlerts;
import view.components.ITableView;
import view.components.TablePagerView;
import view.components.impl.PatientTableView;

public class PatientTablePagerViewPresenter implements TablePagerViewPresenter<Patient> {
    private final TablePagerView<Patient> patientTablePagerView;
    private final PatientPresentersMediator patientPresentersMediator;
    private CacheableModel<Patient> model;
    private PatientTableView currentPage;
    private Patient selectedPatient;

    public PatientTablePagerViewPresenter
            (TablePagerView<Patient> patientTablePagerView,
             PatientPresentersMediator patientPresentersMediator) {
        this.patientTablePagerView = patientTablePagerView;
        this.patientPresentersMediator = patientPresentersMediator;
        loadView();
    }

    public PatientTablePagerViewPresenter
            (TablePagerView<Patient> patientTablePagerView,
             CacheableModel<Patient> model,
             PatientPresentersMediator patientPresentersMediator) {
        this.model = model;
        this.patientTablePagerView = patientTablePagerView;
        this.patientPresentersMediator = patientPresentersMediator;
        loadView();
    }

    @Override
    public CacheableModel<Patient> getModel() {
        return model;
    }

    @Override
    public void setModel(IModel model) {
        if (model instanceof CacheableModel) {
            this.model = ((CacheableModel) model);
        }
    }

    @Override
    public void loadView() {
        patientTablePagerView.setTablePagerViewPresenter(this);
    }

    @Override
    public void onAmountOfCellsChanged() {
        patientPresentersMediator.onUpdate();
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
        this.selectedPatient = selectedItem;
        patientPresentersMediator.showSelected(selectedItem);
    }

    @Override
    public TableView<Patient> getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(ITableView tableView) {
        this.currentPage = ((PatientTableView) tableView);
    }

    @Override
    public boolean updateCache(int offset, int limit) {
        model.get(Document.parse(model.getCurrentState()), offset, limit);
        //TODO wasCacheUpdated
        return true;
    }

    @Override
    public Patient getSelectedItem() {
        return this.selectedPatient;
    }

    @Override
    public void setSelectedItem(Patient item) {
        this.selectedPatient = item;
    }

    @Override
    public int getRowsPerPage() {
        return this.patientTablePagerView.getRowsPerPage();
    }

    @Override
    public void setRowsPerPage(int rowsPerPage) {
        this.patientTablePagerView.setRowsPerPage(rowsPerPage);
    }
}
