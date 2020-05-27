package client;

import client.response.PostResponseMessage;
import client.response.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class ConnectionManagerImpl implements ConnectionManager {
    private static final String SERVER_HOST_PROPERTY = "server.host";
    private static final String SERVER_ROOT_PROPERTY = "server.root";
    private static final String DATABASE_COLLECTION_PROPERTY = "db.collection";
    private static final String PROPERTY_PATH = "src/main/resources/config.properties";

    private final Properties properties;

    {
        properties = new Properties();
    }

    public ConnectionManagerImpl() {
    }

    @Override
    public RequestExecutorImpl connect() {
        RequestExecutorImpl requestExecutor = null;
        try {
            var fis = new FileInputStream(PROPERTY_PATH);
            properties.load(fis);
            requestExecutor = new RequestExecutorImpl(
                    properties.getProperty(SERVER_HOST_PROPERTY, "localhost:8080"),
                    properties.getProperty(SERVER_ROOT_PROPERTY, "/hospitalApp/hospitalDB/"),
                    properties.getProperty(DATABASE_COLLECTION_PROPERTY, "patients"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestExecutor;
    }

    @Override
    public RequestExecutorImpl reconnect(String collectionName) {
        properties.put(DATABASE_COLLECTION_PROPERTY, collectionName);
        return new RequestExecutorImpl(
                properties.getProperty(SERVER_HOST_PROPERTY, "localhost:8080"),
                properties.getProperty(SERVER_ROOT_PROPERTY, "/hospitalApp/hospitalDB/"),
                collectionName);
    }

    @Override
    public void createCollection(String collection) {
        PostResponseMessage postResponseMessage = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost(properties.getProperty(SERVER_HOST_PROPERTY, "localhost:8080"))
                    .setPath(properties.getProperty(SERVER_ROOT_PROPERTY, "/hospitalApp/hospitalDB/")
                            + collection)
                    .build();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("accept", "application/json");
            httpPost.setEntity(new StringEntity(" "));
            var httpResponse = httpClient.execute(httpPost);
            //TODO вывод кода ответа
            postResponseMessage = ResponseHandler.processPostResponse(httpResponse);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropCollection(String collection) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost(properties.getProperty(SERVER_HOST_PROPERTY, "localhost:8080"))
                    .setPath(properties.getProperty(SERVER_ROOT_PROPERTY, "/hospitalApp/hospitalDB/")
                            + collection)
                    .build();
            HttpDelete httpDelete = new HttpDelete(uri);
            httpDelete.addHeader("accept", "application/text");
            var httpResponse = httpClient.execute(httpDelete);
            //TODO вывод кода ответа
            var deleteResponseMessage = ResponseHandler.processDeleteResponse(httpResponse);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
