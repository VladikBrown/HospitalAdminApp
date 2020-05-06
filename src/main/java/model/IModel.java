package model;

import javafx.collections.ObservableList;
import org.bson.Document;

public interface IModel<T> {
    void upload();

    void find(Document document);

    void add(T item);

    void delete(T selectedPatient);

    int deleteAll(Document document);

    ObservableList<T> getAll();
}
