package model;

import client.MyClient;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entity.Patient;
import mongoDAO.PatientDAO;
import mongoDAO.impl.PatientDAOImpl;
import org.bson.Document;
import presenter.PatientPresenter;

import java.util.Arrays;
import java.util.List;

//играет роль Service
//TODO singleton
//TODO сделать модификацию бд более безопасной
public class PatientModel implements IModel<Patient> {
    private final PatientDAO patientDAO;
    private ObservableList<Patient> patients;
    MyClient myClient;
    private PatientPresenter patientPresenter;

    public PatientModel() {
        patients = FXCollections.observableArrayList();
        patientDAO = new PatientDAOImpl();
    }

    public PatientModel(PatientPresenter patientPresenter) {
        //connect();
        this.patientPresenter = patientPresenter;
        patients = FXCollections.observableArrayList();
        patientDAO = new PatientDAOImpl();
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    public void setPatientPresenter(PatientPresenter patientPresenter) {
        this.patientPresenter = patientPresenter;
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

    public void add(Patient patient) {
        patientDAO.add(patient);
        patients.add(patient);
        patientPresenter.onUpdate();
    }

    public void setPatients(ObservableList<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public void updateCache(int offset, int limit) {

    }

    // вот это уродище-костылище ахахахахахахах
    public void find(Document document) {
        List<Patient> patients = stringToArray(MyClient.doGet("patients", document), Patient[].class);
        patientPresenter.showTable(FXCollections.observableList(patients));
    }
}
