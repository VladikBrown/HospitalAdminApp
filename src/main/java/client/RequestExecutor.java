package client;

import client.response.IMessage;
import org.bson.Document;

public interface RequestExecutor {
    IMessage doGet(Document document, int offset, int limit);

    IMessage doPost(Document document);

    IMessage doDelete(Document document);
}
