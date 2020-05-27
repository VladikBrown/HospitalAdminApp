package mongoDAO;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
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
import server.exceptions.CollectionNotFoundException;
import server.exceptions.NoSuchDataBaseException;

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
    private static final String CREATING_COLLECTION_ERROR_MESSAGE = "Failed to create new collection";
    private static final String DROP_COLLECTION_ERROR_MESSAGE = "Failed to drop existing collection";

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
    private String currentCollectionName;
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

    public WorkWithMongo(String dbname) throws NoSuchDataBaseException {
        currentDatabaseName = dbname;
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        connectionString = new ConnectionString("mongodb://localhost");
        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        try (var mongoClient = MongoClients.create(clientSettings)) {

            if (!isDataBaseExist(dbname, mongoClient)) {
                throw new NoSuchDataBaseException("database not found");
            }
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            LOGGER.log(Level.INFO, "Connection established");
        }
    }

    /**
     * connects to selected database and collection
     */
    public WorkWithMongo(String dbname, String collectionName) throws CollectionNotFoundException, NoSuchDataBaseException {
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
            if (!isDataBaseExist(dbname, mongoClient)) {
                throw new NoSuchDataBaseException("database not found");
            }
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            if (!isCollectionExist(collectionName, mongoClient)) {
                throw new CollectionNotFoundException("collection not found");
            }
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            LOGGER.log(Level.INFO, "Connection established");
        }
    }

    public void add(Patient patient) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.insertOne(patient);
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

    public ObservableList<Patient> getAll(int offset, int limit) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find().skip(offset).limit(limit).forEach((Consumer<Patient>) patientsList::add);
            currentCollection.find().skip(offset).limit(limit).forEach((Consumer<Patient>) System.out::println);
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

    //TODO rename -> get
    public ObservableList<Patient> find(Document document, int offset, int limit) {
        ObservableList<Patient> patientsList = FXCollections.observableArrayList();
        if (document.isEmpty()) {
            return patientsList;
        }

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            currentCollection.find(document).skip(offset).limit(limit).forEach((Consumer<Patient>) patientsList::add);
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

    public long getNumberOfRecords(Document document) {
        long numberOfRecords = 0;

        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
            numberOfRecords = currentCollection.countDocuments(document);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, GETTING_DATA_ERROR_MESSAGE, e);
        }

        System.out.println("records: " + numberOfRecords);
        return numberOfRecords;
    }

    public void createCollection(String collection) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            if (!isCollectionExist(collection, mongoClient)) {
                currentDataBase = mongoClient.getDatabase(currentDatabaseName);
                currentDataBase.createCollection(collection);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, CREATING_COLLECTION_ERROR_MESSAGE, e);
        }
    }

    public void dropCollection(String collection) {
        try (var mongoClient = MongoClients.create(clientSettings)) {
            currentDataBase = mongoClient.getDatabase(currentDatabaseName);
            if (isCollectionExist(collection, mongoClient)) {
                currentCollection = currentDataBase.getCollection(currentCollectionName, Patient.class);
                currentCollection.drop();
            } else throw new CollectionNotFoundException("failed to find " + collection + "in" + currentDatabaseName);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, DROP_COLLECTION_ERROR_MESSAGE, e);
        }
    }

    public boolean isDataBaseExist(String dataBaseName, MongoClient mongoClient) {
        boolean isExist = false;
        for (String s : mongoClient.listDatabaseNames()) {
            if (s.equals(dataBaseName)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public boolean isCollectionExist(String collectionName, MongoClient mongoClient) {
        boolean isExist = false;
        var currentDataBase = mongoClient.getDatabase(currentDatabaseName);
        for (String s : currentDataBase.listCollectionNames()) {
            if (s.equals(collectionName)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
