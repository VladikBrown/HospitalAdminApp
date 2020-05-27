package model;

import client.RequestExecutor;
import client.response.DeleteResponseMessage;
import client.response.GetResponseMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entity.Patient;
import org.bson.Document;
import presenter.ModelInteractor;
import util.JsonUtil;
import view.alerts.HttpAllerts;

import java.util.List;
import java.util.Properties;


public class PatientModel implements CacheableModel<Patient> {

    private static final int DEFAULT_LIMIT = 5;
    private final RequestExecutor requestExecutor;
    private ModelInteractor modelInteractor;
    private Properties properties = new Properties();

    private ObservableList<Patient> cache;
    private String lastQuery;
    private int currentTotalNumberOfRecords;
    private int currentLimit;

    {
        this.currentLimit = DEFAULT_LIMIT;
        this.lastQuery = "";
        this.currentTotalNumberOfRecords = 0;
        this.cache = FXCollections.observableArrayList();
    }


    public PatientModel(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public PatientModel(ModelInteractor modelInteractor, RequestExecutor requestExecutor) {
        this.modelInteractor = modelInteractor;
        this.requestExecutor = requestExecutor;
    }

    public void setInteractor(ModelInteractor patientPresenter) {
        this.modelInteractor = patientPresenter;
    }

    @Override
    public void delete(Patient patient) {
        DeleteResponseMessage deleteResponseMessage = (DeleteResponseMessage) requestExecutor.doDelete(new Document(FieldsDB.ID, patient.getId()));
        if (deleteResponseMessage.isSuccessful()) {
            dropCache();
            dropAndUpload();
        } else {
            HttpAllerts.basicHttpInfoAlert(deleteResponseMessage.getStatusCode(), deleteResponseMessage.getReasonPhrase());
        }
    }

    @Override
    public int deleteAll(Document document) {
        DeleteResponseMessage deleteResponseMessage = (DeleteResponseMessage) requestExecutor.doDelete(document);
        if (deleteResponseMessage.isSuccessful()) {
            dropCache();
            dropAndUpload();
            return deleteResponseMessage.getNumberOfDeletedRecords();
        } else {
            HttpAllerts.basicHttpInfoAlert(deleteResponseMessage.getStatusCode(), deleteResponseMessage.getReasonPhrase());
            return -1;
        }
    }

    @Override
    public void dropAndUpload() {
        if ("{}".equals(getCurrentState())) {
            dropCache();
        }
        Document query = Document.parse("{}");
        setCurrentState("{}");
        GetResponseMessage response = (GetResponseMessage) requestExecutor.doGet(query, 0, currentLimit);
        if (response.isSuccessful()) {
            this.currentTotalNumberOfRecords = Integer.parseInt(response.getNumberOfRecords());
            cache.addAll(JsonUtil.stringToArray(response.getJsonEntity(), Patient[].class));
        } else {
            HttpAllerts.basicHttpInfoAlert(response.getStatusCode(), response.getReasonPhrase());
        }
    }

    public ObservableList<Patient> getAll() {
        setCurrentState("{}");
        if (cache.isEmpty()) {
            dropAndUpload();
        }
        return cache;
    }

    @Override
    public void updateCache(List<? extends Patient> newRecords) {
        cache.addAll(newRecords);
    }

    @Override
    public void dropCache() {
        cache.clear();
    }

    @Override
    public void add(Patient patient) {
        requestExecutor.doPost(Document.parse(JsonUtil.toJson(patient)));
        dropCache();
        modelInteractor.onModelUpdated();
    }

    // вот это уродище-костылище ахахахахахахах
    @Override
    public void get(Document document, int offset, int limit) {
        currentLimit = limit;
        if (isNewQuery(document)) {
            dropCache();
            setCurrentState(document.toJson());
        }
        GetResponseMessage response = (GetResponseMessage) requestExecutor.doGet(document, offset, limit);
        if (response.isSuccessful()) {
            this.currentTotalNumberOfRecords = Integer.parseInt(response.getNumberOfRecords());
            cache.addAll(JsonUtil.stringToArray(response.getJsonEntity(), Patient[].class));
        } else {
            HttpAllerts.basicHttpInfoAlert(response.getStatusCode(), response.getReasonPhrase());
        }
    }

    @Override
    public int getCurrentTotalNumberOfRecords() {
        return currentTotalNumberOfRecords;
    }

    @Override
    public void setCurrentTotalNumberOfRecords(int currentTotalNumberOfRecords) {
        this.currentTotalNumberOfRecords = currentTotalNumberOfRecords;
    }

    public ObservableList<Patient> getCache() {
        return cache;
    }

    @Override
    public void setCache(List<? extends Patient> cache) {
        this.cache = (ObservableList<Patient>) cache;
    }

    @Override
    public String getCurrentState() {
        return lastQuery;
    }

    @Override
    public void setCurrentState(String state) {
        this.lastQuery = state;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    public boolean isNewQuery(Document document) {
        return !lastQuery.equals(document.toJson());
    }
}
