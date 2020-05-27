package model;

import javafx.collections.ObservableList;
import org.bson.Document;

public interface IModel<T> {

    void dropAndUpload();

    void get(Document document, int offset, int limit);

    void add(T item);

    void delete(T selectedPatient);

    int deleteAll(Document document);

    ObservableList<T> getAll();

}
