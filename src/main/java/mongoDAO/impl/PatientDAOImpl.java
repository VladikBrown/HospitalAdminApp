package mongoDAO.impl;

import javafx.collections.ObservableList;
import model.entity.Patient;
import mongoDAO.PatientDAO;
import mongoDAO.WorkWithMongo;
import org.bson.Document;
import server.exceptions.CollectionNotFoundException;
import server.exceptions.NoSuchDataBaseException;

public class PatientDAOImpl implements PatientDAO {
    private final WorkWithMongo workWithMongo;

    public PatientDAOImpl() {
        workWithMongo = new WorkWithMongo();
    }

    public PatientDAOImpl(String path) throws CollectionNotFoundException, NoSuchDataBaseException {
        String[] dividedPath = path.split("/");
        for (var s : dividedPath) {
            System.out.println(s);
        }
        workWithMongo = new WorkWithMongo(dividedPath[2], dividedPath[3]);
    }

    @Override
    public ObservableList<Patient> find(Document document, int offset, int limit) {
        return workWithMongo.find(document, offset, limit);
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
    public ObservableList<Patient> getAll(int offset, int limit) {
        return workWithMongo.getAll(offset, limit);
    }

    @Override
    public long getNumberRecords(Document document) {
        return workWithMongo.getNumberOfRecords(document);
    }
}
