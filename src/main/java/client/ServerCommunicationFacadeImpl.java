package client;

import client.response.DeleteResponseMessage;
import client.response.GetResponseMessage;
import client.response.PostResponseMessage;
import org.bson.Document;

public class ServerCommunicationFacadeImpl implements ServerCommunicationFacade {
    RequestExecutor requestExecutor;
    ConnectionManager connectionManager;

    public ServerCommunicationFacadeImpl() {
        connectionManager = new ConnectionManagerImpl();
        requestExecutor = connectionManager.connect();
    }

    @Override
    public void connect() {
        requestExecutor = connectionManager.connect();
    }

    @Override
    public void reconnect(String collectionName) {
        requestExecutor = connectionManager.reconnect(collectionName);
    }

    @Override
    public void createCollection(String string) {
        connectionManager.createCollection(string);
    }

    @Override
    public void dropCollection(String string) {
        connectionManager.dropCollection(string);
    }

    @Override
    public GetResponseMessage doGet(Document document, int offset, int limit) {
        return (GetResponseMessage) requestExecutor.doGet(document, offset, limit);
    }

    @Override
    public PostResponseMessage doPost(Document document) {
        return (PostResponseMessage) requestExecutor.doPost(document);
    }

    @Override
    public DeleteResponseMessage doDelete(Document document) {
        return (DeleteResponseMessage) requestExecutor.doDelete(document);
    }
}
