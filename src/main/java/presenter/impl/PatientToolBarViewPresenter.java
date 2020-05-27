package presenter.impl;

import javafx.collections.ObservableList;
import model.CacheableModel;
import model.IModel;
import model.entity.Patient;
import org.bson.Document;
import presenter.eventHandlers.ConnectionManagerEventHandler;
import presenter.mediator.PatientPresentersMediator;
import view.alerts.PatientAlerts;
import view.components.IToolBarView;
import view.components.impl.ConnectionManagerView;
import view.components.impl.ToolBarView;
import view.dialogs.AddPatientDialog;
import view.dialogs.FindPatientDialog;

import java.util.Optional;

public class PatientToolBarViewPresenter implements presenter.ToolBarViewPresenter {
    private final IToolBarView toolBarView;
    private final PatientPresentersMediator patientPresentersMediator;
    private CacheableModel<Patient> model;
    private ConnectionManagerView connectionManagerView;
    private boolean isChangeCollectionActive = false;

    public PatientToolBarViewPresenter
            (CacheableModel<Patient> model,
             IToolBarView toolBarView,
             PatientPresentersMediator patientPresentersMediator) {
        this.model = model;
        this.toolBarView = toolBarView;
        this.patientPresentersMediator = patientPresentersMediator;
        loadView();
        configureConnectionManagerView();
    }

    @Override
    public void onUpdate() {
        patientPresentersMediator.showTable(model.getAll(), model.getCurrentTotalNumberOfRecords());
    }

    @Override
    public void onDelete() {
        if (patientPresentersMediator.getSelectedPatient() != null) {
            model.delete(patientPresentersMediator.getSelectedPatient());
            patientPresentersMediator.getCurrentPage().getItems().removeAll(patientPresentersMediator.getSelectedPatient());
            patientPresentersMediator.setSelectedPatient(null);
        } else {
            PatientAlerts.notSelectedError();
        }
    }

    @Override
    public void onAdd() {
        var patientDialog = new AddPatientDialog();
        patientDialog.show();
        patientDialog.getResult().ifPresent(patient -> model.add(patient));
    }

    @Override
    public void onFind() {
        var patientDialog = new FindPatientDialog();
        patientDialog.show();
        patientDialog.getResult().ifPresent(document -> model.get(document, 0, patientPresentersMediator.getRowsPerPage()));
        patientPresentersMediator.showTable((ObservableList<Patient>) model.getCache(), model.getCurrentTotalNumberOfRecords());
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

    private void configureConnectionManagerView() {
        connectionManagerView = new ConnectionManagerView(new ConnectionManagerEventHandler() {
            @Override
            public void onCreate(String collection) {
                patientPresentersMediator.getConnectionManager().createCollection(collection);
            }

            @Override
            public void onDelete(String collection) {
                patientPresentersMediator.getConnectionManager().dropCollection(collection);
            }
        });
    }

    @Override
    public void onChangeCollection() {
        if (!isChangeCollectionActive) {
            ((ToolBarView) toolBarView).getItems().add(connectionManagerView.asNode());
        } else {
            patientPresentersMediator.getConnectionManager().reconnect(connectionManagerView.getText());
            model.dropCache();
            ((ToolBarView) toolBarView).getItems().remove(connectionManagerView.asNode());
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
            this.model = (CacheableModel) model;
        }
    }

    @Override
    public void loadView() {
        this.toolBarView.setToolBarViewPresenter(this);
    }
}
