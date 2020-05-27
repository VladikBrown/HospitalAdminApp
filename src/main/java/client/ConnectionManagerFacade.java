package client;

public interface ConnectionManagerFacade {
    void connect();

    void reconnect(String collectionName);

    void createCollection(String string);

    void dropCollection(String string);
}
