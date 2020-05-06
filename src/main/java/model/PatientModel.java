package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entity.Patient;
import mongoDAO.PatientDAO;
import mongoDAO.impl.PatientDAOImpl;
import org.bson.Document;
import presenter.Presenter;

//играет роль Service
//TODO singleton
//TODO сделать модификацию бд более безопасной
public class PatientModel implements IModel<Patient> {
    private Presenter presenter;
    private ObservableList<Patient> patients;
    private PatientDAO patientDAO;

    public PatientModel() {
        patients = FXCollections.observableArrayList();
        patientDAO = new PatientDAOImpl();
    }

    public PatientModel(Presenter presenter) {
        this.presenter = presenter;
        patients = FXCollections.observableArrayList();
        patientDAO = new PatientDAOImpl();
    }

    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }

    public void add(Patient patient) {
        patientDAO.add(patient);
        patients.add(patient);
        presenter.onUpdate();
    }

    public void delete(Patient patient) {
        patients.removeAll(patient);
        patientDAO.delete(patient);
    }


    public int deleteAll(Document document) {
        int deletedPatients = patientDAO.deleteAll(document);
        upload();
        return deletedPatients;
    }

    @Override
    public void upload() {
        patients = patientDAO.getAll();
    }

    public ObservableList<Patient> getAll() {
        if (patients.isEmpty()) {
            upload();
        }
        return patients;
    }

    public void setPatients(ObservableList<Patient> patients) {
        this.patients = patients;
    }

    public void find(Document document) {
        presenter.showTable(patientDAO.find(document));
    }
}
