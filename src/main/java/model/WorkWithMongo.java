package model;

import com.mongodb.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.types.ObjectId;
import util.DateUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class WorkWithMongo {
    private DB database;


    private DBCollection table;

    public WorkWithMongo() {
        try(var mongoClient = new MongoClient()) {
            database = mongoClient.getDB("hospitalDB");
            table = database.getCollection("patients");
            Patient patient = new Patient("Brown", "Vlad", "V", new Address("Minsk", 33, 2, 4),
                    new Date("1998-12-13"), new Date("2020-10-28"), "House", "Dickhead");
            //add(patient);
            table.find().forEach(System.out::println);
        }
    }
    //TODO: добавить запись add(Patient patient)
    public void add(Patient patient){
        try(var mongoClient = new MongoClient()) {
            BasicDBObject document = new BasicDBObject();
            document.append(FieldsDB.SURNAME, patient.getSurname())
                    .append(FieldsDB.FIRST_NAME, patient.getFirstName())
                    .append(FieldsDB.SECOND_NAME, patient.getSecondName())
                    .append(FieldsDB.BIRTHDATE, patient.getBirthdate())
                    .append(FieldsDB.DATE_OF_VISIT, patient.getDateOfVisit())
                    .append(FieldsDB.NAME_OF_DOCTOR, patient.getDocName())
                    .append(FieldsDB.CONCLUSION, patient.getConclusion());
            Address address = patient.getAddress();
            BasicDBObject addressDBO = new BasicDBObject();
            addressDBO.append(FieldsDB.STREET, address.getStreet())
                    .append(FieldsDB.HOME_NUMBER, address.getHomeNumber())
                    .append(FieldsDB.BUILDING_NUMBER, address.getBuildingNumber())
                    .append(FieldsDB.APARTMENTS_NUMBER, address.getApartmentsNumber());
            document.append(FieldsDB.ADDRESS, addressDBO);
            table.insert(document);
        }
    }

    public Patient get(String _id){
        Patient patient = new Patient();
        try(var mongoClient = new MongoClient()) {

            BasicDBObject query = new BasicDBObject();
            query.put(FieldsDB.ID, _id);
            Optional<DBObject> result = Optional.ofNullable(table.findOne(query));
            if(result.isPresent()){
                patient = toPatient(result.get());
            }
            //TODO: написать свой эксеспшен
        } catch (Exception e){
            System.err.println("Failed to get data from DB");
        }
        return patient;
    }

    //TODO: поиск по адресу

    /**
     * Не подходит для поиска по адресу (для этого есть отдельный метод)
     */
    public ObservableList<Patient> get(String key, Object value){
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        BasicDBObject query = new BasicDBObject();
        switch (key){
            case FieldsDB.ID -> query.put(FieldsDB.ID, String.valueOf(value));
            case FieldsDB.SURNAME -> query.put(FieldsDB.SURNAME, String.valueOf(value));
            case FieldsDB.FIRST_NAME -> query.put(FieldsDB.FIRST_NAME, String.valueOf(value));
            case FieldsDB.SECOND_NAME -> query.put(FieldsDB.SECOND_NAME, String.valueOf(value));
            case FieldsDB.BIRTHDATE -> query.put(FieldsDB.BIRTHDATE, DateUtil.format((LocalDate) value));
            case FieldsDB.DATE_OF_VISIT -> query.put(FieldsDB.DATE_OF_VISIT, DateUtil.format((LocalDate) value));
            case FieldsDB.NAME_OF_DOCTOR -> query.put(FieldsDB.NAME_OF_DOCTOR, String.valueOf(value));
            case FieldsDB.CONCLUSION -> query.put(FieldsDB.CONCLUSION, String.valueOf(value));
        }
        try(var mongoClient = new MongoClient()) {
            Optional<DBCursor> result = Optional.of(table.find(query));
            result.ifPresent(dbObjects -> dbObjects.forEach(dbObject -> patients.add(toPatient(dbObject))));
        } catch (Exception e){
            System.err.println("Failed to get data from DB");
        }
        //менее безопасный вариант
        //DBCursor results = table.find(query);
        //results.forEach(dbObject -> patients.add(toPatient(dbObject)));
        return patients;
    }

    private Patient toPatient(DBObject dbObject){
        Patient patient = new Patient();
        Address address = new Address();
        /*
        DBObject resAddress = (DBObject) dbObject.get(FieldsDB.ADDRESS);

        address.setStreet(String.valueOf(resAddress.get(FieldsDB.STREET)));
        address.setHomeNumber(Integer.valueOf(String.valueOf(resAddress.get(FieldsDB.HOME_NUMBER))));
        address.setBuildingNumber(Integer.valueOf(String.valueOf(resAddress.get(FieldsDB.BUILDING_NUMBER))));
        address.setApartmentsNumber(Integer.valueOf(String.valueOf(resAddress.get(FieldsDB.APARTMENTS_NUMBER))));
         */
        patient.setId(new ObjectId(String.valueOf(dbObject.get(FieldsDB.ID))));
        patient.setSurname(String.valueOf(dbObject.get(FieldsDB.SURNAME)));
        patient.setFirstName(String.valueOf(dbObject.get(FieldsDB.FIRST_NAME)));
        patient.setSecondName(String.valueOf(dbObject.get(FieldsDB.SECOND_NAME)));
        patient.setAddress(address);
        patient.setBirthdate(new Date(String.valueOf(dbObject.get(FieldsDB.BIRTHDATE))));
        patient.setDateOfVisit(new Date(String.valueOf(dbObject.get(FieldsDB.DATE_OF_VISIT))));
        patient.setDocName(String.valueOf(dbObject.get(FieldsDB.NAME_OF_DOCTOR)));
        patient.setConclusion(String.valueOf(dbObject.get(FieldsDB.CONCLUSION)));
        return patient;
    }

    public ObservableList<Patient> getAllPatients() {
         ObservableList<Patient> patients = FXCollections.observableArrayList();
        try(var mongoClient = new MongoClient()) {
            database = mongoClient.getDB("hospitalDB");
            table = database.getCollection("patients");
            Optional<DBCursor> result = Optional.of(table.find());
            result.ifPresent(dbObjects -> dbObjects.forEach(dbObject -> patients.add(toPatient(dbObject))));
        }

         return  patients;
    }
    //TODO: удалить запись delete(Patient patient)
    //TODO: Найти по:
    /*
      фамилии
      адресу
      по дате
      по фио врача
      по дате ласт приема
     */
    //TODO: изменить change(Patient patient)
}

