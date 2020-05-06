package mongoDAO.impl;

import javafx.collections.ObservableList;
import model.entity.Patient;
import mongoDAO.PatientDAO;
import mongoDAO.WorkWithMongo;
import org.bson.Document;

public class PatientDAOImpl implements PatientDAO {
    private WorkWithMongo workWithMongo;

    {
        workWithMongo = new WorkWithMongo();
    }

    @Override
    public ObservableList<Patient> find(Document document) {
        return workWithMongo.find(document);
    }

    @Override
    public void add(Patient item) {
        workWithMongo.add(item);
    }

    @Override
    public void delete(Patient selectedPatient) {
        workWithMongo.delete(selectedPatient);
    }

    @Override
    public int deleteAll(Document document) {
        return workWithMongo.deleteAll(document);
    }

    @Override
    public ObservableList<Patient> getAll() {
        return workWithMongo.getAll();
    }
}
