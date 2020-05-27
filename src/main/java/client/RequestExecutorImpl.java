package client;

import client.response.DeleteResponseMessage;
import client.response.GetResponseMessage;
import client.response.PostResponseMessage;
import client.response.ResponseHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bson.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class RequestExecutorImpl implements RequestExecutor {
    private final String host; // localhost:8080
    private final String path; // /hospitalApp/hospitalDB/
    private final String collection; //patients

    public RequestExecutorImpl(String host, String path, String collection) {
        this.host = host;
        this.path = path;
        this.collection = collection;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GetResponseMessage doGet(Document document, int offset, int limit) {
        GetResponseMessage getResponseMessage = null;
        List<NameValuePair> parameters = new LinkedList<>();
        document.forEach((key, value) -> parameters.add(new BasicNameValuePair(key, String.valueOf(value))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost(this.host)
                    .setPath(this.path + this.collection)
                    .addParameter("offset", String.valueOf(offset))
                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(parameters)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("accept", "application/json");
            var httpResponse = httpClient.execute(httpGet);
            getResponseMessage = ResponseHandler.processGetResponse(httpResponse);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return getResponseMessage;
    }

    public DeleteResponseMessage doDelete(Document document) {
        List<NameValuePair> parameters = new LinkedList<>();
        document.forEach((key, value) -> parameters.add(new BasicNameValuePair(key, String.valueOf(value))));
        DeleteResponseMessage deleteResponseMessage = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost(host)
                    .setPath(path + collection)
                    .addParameters(parameters)
                    .build();
            HttpDelete httpDelete = new HttpDelete(uri);
            httpDelete.addHeader("accept", "application/text");
            var httpResponse = httpClient.execute(httpDelete);
            deleteResponseMessage = ResponseHandler.processDeleteResponse(httpResponse);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return deleteResponseMessage;
    }

    public PostResponseMessage doPost(Document document) {
        PostResponseMessage postResponseMessage = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost(host)
                    .setPath(path + collection)
                    .build();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("accept", "application/json");
            httpPost.setEntity(new StringEntity(document.toJson()));
            var httpResponse = httpClient.execute(httpPost);
            postResponseMessage = ResponseHandler.processPostResponse(httpResponse);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return postResponseMessage;
    }
}
