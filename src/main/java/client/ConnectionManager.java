package client;

public interface ConnectionManager {
    RequestExecutor connect();

    RequestExecutor reconnect(String collectionName);

    void createCollection(String string);

    void dropCollection(String string);
}
