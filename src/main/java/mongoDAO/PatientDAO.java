package mongoDAO;

import javafx.collections.ObservableList;
import model.entity.Patient;
import org.bson.Document;

public interface PatientDAO {
    ObservableList<Patient> find(Document document);

    void add(Patient item);

    void delete(Patient selectedPatient);

    int deleteAll(Document document);

    ObservableList<Patient> getAll();
}
