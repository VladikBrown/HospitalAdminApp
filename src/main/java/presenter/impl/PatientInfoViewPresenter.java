package presenter.impl;

import model.IModel;
import model.entity.Patient;
import presenter.InfoViewPresenter;
import presenter.mediator.PatientPresentersMediator;
import view.components.InfoView;

public class PatientInfoViewPresenter implements InfoViewPresenter<Patient> {
    private final PatientPresentersMediator patientPresentersMediator;
    private InfoView<Patient> patientInfoView;
    private IModel model;


    public PatientInfoViewPresenter
            (InfoView<Patient> patientInfoView,
             IModel model,
             PatientPresentersMediator patientPresentersMediator) {
        this.patientInfoView = patientInfoView;
        this.model = model;
        this.patientPresentersMediator = patientPresentersMediator;
        loadView();
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
        patientInfoView.showInfo(patient);
    }

    @Override
    public IModel getModel() {
        return null;
    }

    @Override
    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public void loadView() {
        this.patientInfoView.setInfoViewPresenter(this);
    }
}
