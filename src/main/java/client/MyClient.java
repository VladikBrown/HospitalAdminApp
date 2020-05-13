package client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class MyClient {
    public MyClient() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String doGet(String collection, Document document) {
        String result = "";
        List<NameValuePair> parameters = new LinkedList<>();
        document.forEach((key, value) -> parameters.add(new BasicNameValuePair(key, String.valueOf(value))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost("localhost:8080")
                    .setPath("/hospitalApp/hospitalDB/" + collection)
                    .addParameters(parameters)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("accept", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((httpResponse.getEntity().getContent())));
            result = br.readLine();

            System.out.println(result);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doDelete(String collection, Document document) {
        String result = "";
        List<NameValuePair> parameters = new LinkedList<>();
        document.forEach((key, value) -> parameters.add(new BasicNameValuePair(key, String.valueOf(value))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().
                    setScheme("http")
                    .setHost("localhost:8080")
                    .setPath("/hospitalApp/hospitalDB/" + collection)
                    .addParameters(parameters)
                    .build();
            HttpDelete httpDelete = new HttpDelete(uri);
            httpDelete.addHeader("accept", "application/text");
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((httpResponse.getEntity().getContent())));
            result = br.readLine();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
