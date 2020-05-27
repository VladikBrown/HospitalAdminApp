package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseCreator {
    MyServer myServer;

    public ResponseCreator(MyServer myServer) {
        this.myServer = myServer;
    }

    public void doGetResponse(HttpExchange httpExchange, String response, String numberOfRecords) {
        myServer.myLogger.logMessage("Handling response");
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.getResponseHeaders().add("HAPP-records", numberOfRecords);
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
            myServer.myLogger.logMessage("Response sanded");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
            myServer.myLogger.logMessage("Exchange closed");
        }
    }

    public void doDeleteResponse(HttpExchange httpExchange, String numberOfRecords) {
        myServer.myLogger.logMessage("Handling response");
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.getResponseHeaders().add("HAPP-records", numberOfRecords);
            httpExchange.sendResponseHeaders(200, numberOfRecords.length());
            outputStream.write(numberOfRecords.getBytes());
            outputStream.flush();
            outputStream.close();
            myServer.myLogger.logMessage("Response sanded");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
            myServer.myLogger.logMessage("Exchange closed");
        }
    }

    public void doPostResponse(HttpExchange httpExchange) {
        myServer.myLogger.logMessage("Handling response");
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.sendResponseHeaders(200, 0);
            outputStream.flush();
            outputStream.close();
            myServer.myLogger.logMessage("Response sanded");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
            myServer.myLogger.logMessage("Exchange closed");
        }
    }

    public void doErrorResponse(HttpExchange httpExchange, int errorCode) {
        myServer.myLogger.logMessage("Sending error response");
        OutputStream outputStream = httpExchange.getResponseBody();
        try {
            httpExchange.sendResponseHeaders(errorCode, 0);
            outputStream.flush();
            outputStream.close();
            myServer.myLogger.logMessage("Response sanded");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
            myServer.myLogger.logMessage("Exchange closed");
        }
    }
}
