package mongoDAO;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FieldsDB;
import model.entity.Patient;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.FileInputStream;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class WorkWithMongo {
    static Logger LOGGER;
    private static final String DEFAULT_DB_NAME = "hospitalDB";
    private static final String DEFAULT_COLLECTION_NAME = "patients";
    private static final String GETTING_DATA_ERROR_MESSAGE = "Failed to get data from DB";
    private static final String DELETING_DATA_ERROR_MESSAGE = "Failed to delete data from DB";
    private static final String ADDING_DATA_ERROR_MESSAGE = "Failed to add data to DB";

    static {
        try (FileInputStream ins = new FileInputStream("./src/main/java/log/log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(WorkWithMongo.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    private final ConnectionString connectionString;
    private final CodecRegistry pojoCodecRegistry;
    private final CodecRegistry codecRegistry;
    private final MongoClientSettings clientSettings;
    private final String currentDatabaseName;
    private final String currentCollectionName;
    private MongoCollection<Patient> currentCollection;
    private MongoDatabase currentDataBase;

    /**
     * connects to localhost and default path (see consts)
     */
    public WorkWithMongo() {
        currentDatabaseName = DEFAULT_DB_NAME;
        currentCollectionName = DEFAULT_COLLECTION_NAME;
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        connectionString = new ConnectionString("mongodb://localhost");
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            LOGGER.log(Level.INFO, "Default connection established");
        }
    }

    /**
     * connects to selected database and collection
     */
    public WorkWithMongo(String dbname, String collectionName) {
        currentDatabaseName = dbname;
        currentCollectionName = collectionName;
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        connectionString = new ConnectionString("mongodb://localhost");
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            LOGGER.log(Level.INFO, "Connection established");
        }
    }

    public void add(Patient patient) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.insertOne(patient);
            //TODO классы исключений в пакете Exceptions!!!
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, ADDING_DATA_ERROR_MESSAGE, e);
        }
    }

    public void delete(Patient patient) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            Document patientWithID = new Document(FieldsDB.ID, patient.getId());
            currentCollection.deleteOne(patientWithID);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, DELETING_DATA_ERROR_MESSAGE, e);
        }
    }

    public ObservableList<Patient> getAll() {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find().forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, GETTING_DATA_ERROR_MESSAGE, e);
        }
        return patientsList;
    }

    public ObservableList<Patient> get(String key, Object value) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find(eq(key, value)).forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, GETTING_DATA_ERROR_MESSAGE, e);
        }
        return patientsList;
    }

    public ObservableList<Patient> find(Document document) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        if (document.isEmpty()) {
            return patientsList;
        }

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find(document).forEach((Consumer<Patient>) patientsList::add);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, GETTING_DATA_ERROR_MESSAGE, e);
        }
        return patientsList;
    }

    public int deleteAll(Document document) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        int amountOfPatient = 0;
        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find(document).forEach((Consumer<Patient>) patientsList::add);
            amountOfPatient = patientsList.size();
            currentCollection.deleteMany(document);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, GETTING_DATA_ERROR_MESSAGE, e);
        }
        return amountOfPatient;
    }
}
