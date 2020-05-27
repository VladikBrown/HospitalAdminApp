package client.response;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ResponseHandler {

    public static GetResponseMessage processGetResponse(HttpResponse httpResponse) {
        String statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
        if (statusCode.startsWith("4")
                || statusCode.startsWith("5")) {
            return new GetResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase());
        } else {
            String jsonEntity = "";
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((httpResponse.getEntity().getContent())));
                jsonEntity = br.readLine();
                System.out.println(jsonEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GetResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase(), jsonEntity, httpResponse.getFirstHeader("HAPP-records").getValue());
        }
    }

    public static DeleteResponseMessage processDeleteResponse(HttpResponse httpResponse) {
        DeleteResponseMessage deleteResponseMessage;
        String statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
        if (statusCode.startsWith("4")
                || statusCode.startsWith("5")) {
            return new DeleteResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase());
        } else {
            String jsonEntity = "{}";
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((httpResponse.getEntity().getContent())));
                jsonEntity = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return deleteResponseMessage = new DeleteResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase(), Integer.parseInt(jsonEntity));
        }
    }

    public static PostResponseMessage processPostResponse(HttpResponse httpResponse) {
        PostResponseMessage postResponseMessage;
        String statusCode = Integer.toString(httpResponse.getStatusLine().getStatusCode());
        if (statusCode.startsWith("4")
                || statusCode.startsWith("5")) {
            return new PostResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase());
        } else {
            return postResponseMessage = new PostResponseMessage(statusCode, httpResponse.getStatusLine().getReasonPhrase());
        }
    }
}
