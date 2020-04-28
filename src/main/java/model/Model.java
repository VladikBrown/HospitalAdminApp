package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presenter.Presenter;
//singleton
//TODO: сделать модификацию бд более безопасной
public class Model implements IModel{
    Presenter presenter;
    ObservableList<Patient> patients;
    WorkWithMongo2 workWithMongo2;

    public Model(){
        patients = FXCollections.observableArrayList();
        workWithMongo2 = new WorkWithMongo2();
    }

    public Model(Presenter presenter){
        this.presenter = presenter;
        patients = FXCollections.observableArrayList();
        workWithMongo2 = new WorkWithMongo2();
    }

    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }

    public void addPatient(Patient patient){
        patients.add(patient);
        workWithMongo2.add(patient);
        presenter.setOnUpdateButton();
    }

    public void deletePatient(Patient patient){
        patients.removeAll(patient);
        workWithMongo2.delete(patient);
    }

    @Override
    public void upload(){
        patients = workWithMongo2.getAll();
    }

    public ObservableList<Patient> getAllPatients() {
        if(patients.isEmpty()){
            upload();
        }
        return patients;
    }

    public void setPatients(ObservableList<Patient> patients) {
        this.patients = patients;
    }
}
