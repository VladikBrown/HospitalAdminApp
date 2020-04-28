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

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorkWithMongo2 implements IWorkWithDB{
    ConnectionString connectionString;
    CodecRegistry pojoCodecRegistry;
    CodecRegistry codecRegistry;
    MongoClientSettings clientSettings;
    MongoCollection<Patient> patients;
    MongoDatabase hospitalDB;

    public WorkWithMongo2(){
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        connectionString = new ConnectionString("mongodb://localhost");
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase("hospitalDB");
            patients = hospitalDB.getCollection("patients", Patient.class);
        }
    }

    public void add(Patient patient){
        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase("hospitalDB");
            patients = hospitalDB.getCollection("patients", Patient.class);
            patients.insertOne(patient);
        } catch (Exception e){
            System.err.println("Failed to add data to DB");
        }
    }

    public void delete(Patient patient){
        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase("hospitalDB");
            patients = hospitalDB.getCollection("patients", Patient.class);
            Document patientWithID = new Document(FieldsDB.ID, patient.getId());
            patients.deleteOne(patientWithID);
        } catch (Exception e){
            System.err.println("Failed to delete data from DB");
        }
    }

    public ObservableList<Patient> getAll(){
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase("hospitalDB");
            patients = hospitalDB.getCollection("patients", Patient.class);
            patients.find().forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e){
            System.err.println("Failed to get data from DB");
        }
        return patientsList;
    }

    public ObservableList<Patient> get(String key, Object value){
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            hospitalDB = mongoClient.getDatabase("hospitalDB");
            patients.find(eq(key, value)).forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e){
            System.err.println("Failed to get data from DB");
        }
        return patientsList;
    }

    public ObservableList<Patient> get(Map<String, String> searchCriterions){
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        Set<String> quaries = searchCriterions.keySet();
        return patientsList;
    }
}
