package presenter.mediator;

import client.ConnectionManagerFacade;
import client.ServerCommunicationFacadeImpl;
import com.mongodb.lang.NonNull;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.CacheableModel;
import model.PatientModel;
import model.entity.Patient;
import presenter.InfoViewPresenter;
import presenter.TablePagerViewPresenter;
import presenter.ToolBarViewPresenter;
import presenter.impl.PatientInfoViewPresenter;
import presenter.impl.PatientTablePagerViewPresenter;
import presenter.impl.PatientToolBarViewPresenter;
import view.components.impl.PatientInfoView;
import view.components.impl.PatientTablePagerView;
import view.components.impl.ToolBarView;

public class PatientPresentersMediator implements IMediator {
    private final ConnectionManagerFacade connectionManager;

    private final TablePagerViewPresenter<Patient> patientTablePagerViewPresenter;
    private final ToolBarViewPresenter toolBarViewPresenter;
    private final InfoViewPresenter<Patient> patientInfoViewPresenter;
    private final CacheableModel<Patient> model;


    public PatientPresentersMediator(@NonNull PatientTablePagerView tpv, @NonNull PatientInfoView piv, @NonNull ToolBarView tbv) {
        ServerCommunicationFacadeImpl serverCommunicationFacade = new ServerCommunicationFacadeImpl();
        this.model = new PatientModel(this::onUpdate, serverCommunicationFacade);
        patientTablePagerViewPresenter = new PatientTablePagerViewPresenter(tpv, model, this);
        patientInfoViewPresenter = new PatientInfoViewPresenter(piv, model, this);
        toolBarViewPresenter = new PatientToolBarViewPresenter(model, tbv, this);
        connectionManager = serverCommunicationFacade;
        connectionManager.connect();
    }

    public void onModelUpdated() {
        onUpdate();
    }

    public Patient getSelectedPatient() {
        return patientTablePagerViewPresenter.getSelectedItem();
    }

    public void setSelectedPatient(Patient patient) {
        this.patientTablePagerViewPresenter.setSelectedItem(patient);
    }

    public TablePagerViewPresenter<Patient> getPatientTablePagerViewPresenter() {
        return this.patientTablePagerViewPresenter;
    }

    public InfoViewPresenter<Patient> getPatientInfoViewPresenter() {
        return this.patientInfoViewPresenter;
    }

    public ToolBarViewPresenter getToolBarViewPresenter() {
        return this.toolBarViewPresenter;
    }

    public void onUpdate() {
        toolBarViewPresenter.onUpdate();
    }

    public void showSelected(Patient selectedItem) {
        this.patientInfoViewPresenter.showSelected(selectedItem);
    }

    public void showTable(ObservableList<Patient> all, int currentTotalNumberOfRecords) {
        this.patientTablePagerViewPresenter.showTable(all, currentTotalNumberOfRecords);
    }

    public TableView<Patient> getCurrentPage() {
        return patientTablePagerViewPresenter.getCurrentPage();
    }

    public int getRowsPerPage() {
        return patientTablePagerViewPresenter.getRowsPerPage();
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.patientTablePagerViewPresenter.setRowsPerPage(rowsPerPage);
    }

    public ConnectionManagerFacade getConnectionManager() {
        return connectionManager;
    }
}
