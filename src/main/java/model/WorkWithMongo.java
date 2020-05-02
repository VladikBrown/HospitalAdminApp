package model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorkWithMongo implements IWorkWithDB {
    private static final String DB_NAME = "hospitalDB";
    private static final String COLLECTION_NAME = "patients";
    private static final String GETTING_DATA_ERROR_MESSAGE = "Failed to get data from DB";
    private static final String DELETING_DATA_ERROR_MESSAGE = "Failed to delete data from DB";
    private static final String ADDING_DATA_ERROR_MESSAGE = "Failed to add data to DB";

    ConnectionString connectionString;
    CodecRegistry pojoCodecRegistry;
    CodecRegistry codecRegistry;
    MongoClientSettings clientSettings;
    MongoCollection<Patient> patients;
    MongoDatabase hospitalDB;

    public WorkWithMongo() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        connectionString = new ConnectionString("mongodb://localhost");
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
        }
    }

    public void add(Patient patient) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            patients.insertOne(patient);
        } catch (Exception e) {
            System.err.println(ADDING_DATA_ERROR_MESSAGE);
        }
    }

    public void delete(Patient patient) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            Document patientWithID = new Document(FieldsDB.ID, patient.getId());
            patients.deleteOne(patientWithID);
        } catch (Exception e) {
            System.err.println(DELETING_DATA_ERROR_MESSAGE);
        }
    }

    public ObservableList<Patient> getAll() {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            patients.find().forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            System.err.println(GETTING_DATA_ERROR_MESSAGE);
        }
        return patientsList;
    }

    public ObservableList<Patient> get(String key, Object value) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            patients.find(eq(key, value)).forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            System.err.println(GETTING_DATA_ERROR_MESSAGE);
        }
        return patientsList;
    }

    public ObservableList<Patient> find(Document document) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        if (document.isEmpty()) {
            return patientsList;
        }

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            patients.find(document).forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            System.err.println(GETTING_DATA_ERROR_MESSAGE);
        }
        return patientsList;
    }

    public int deleteAll(Document document) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        int amountOfPatient = 0;
        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase(DB_NAME);
            patients = hospitalDB.getCollection(COLLECTION_NAME, Patient.class);
            patients.find(document).forEach((Consumer<Patient>) patientsList::add);
            amountOfPatient = patientsList.size();
            patients.deleteMany(document);
        } catch (Exception e) {
            System.err.println(DELETING_DATA_ERROR_MESSAGE);
        }
        return amountOfPatient;
    }
}
