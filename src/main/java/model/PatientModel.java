package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import presenter.Presenter;

//TODO singleton
//TODO сделать модификацию бд более безопасной
public class PatientModel implements IModel<Patient> {
    Presenter presenter;
    ObservableList<Patient> patients;
    WorkWithMongo workWithMongo;

    public PatientModel() {
        patients = FXCollections.observableArrayList();
        workWithMongo = new WorkWithMongo();
    }

    public PatientModel(Presenter presenter) {
        this.presenter = presenter;
        patients = FXCollections.observableArrayList();
        workWithMongo = new WorkWithMongo();
    }

    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }

    public void add(Patient patient) {
        patients.add(patient);
        workWithMongo.add(patient);
        presenter.onUpdate();
    }

    public void delete(Patient patient) {
        patients.removeAll(patient);
        workWithMongo.delete(patient);
    }


    public int deleteAll(Document document) {
        int deletedPatients = workWithMongo.deleteAll(document);
        upload();
        return deletedPatients;
    }

    @Override
    public void upload() {
        patients = workWithMongo.getAll();
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
        ObservableList<Patient> patients = workWithMongo.find(document);
        presenter.showTable(patients);
    }
}
