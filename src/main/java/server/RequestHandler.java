package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.entity.Patient;
import mongoDAO.impl.MongoModifier;
import mongoDAO.impl.PatientDAOImpl;
import org.bson.Document;
import server.exceptions.CollectionNotFoundException;
import server.exceptions.NoSuchDataBaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class RequestHandler implements HttpHandler {
    MyServer myServer;
    ResponseCreator responseCreator;

    //TODO взаимодействие должно быть через интерфейс
    RequestHandler(MyServer myServer) {
        this.myServer = myServer;
        this.responseCreator = new ResponseCreator(myServer);
    }

    @Override
    public void handle(HttpExchange exchange) {
        myServer.myLogger.logMessage("Received from client:\n" + exchange.getRequestMethod() + exchange.getRequestURI());
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGetRequest(exchange);
            case "POST" -> handlePostRequest(exchange);
            case "DELETE" -> handleDeleteRequest(exchange);
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) {
        try {
            myServer.myLogger.logMessage("Handling GET request");
            Document query = new Document();
            URI uri = httpExchange.getRequestURI();
            myServer.patientDAO = new PatientDAOImpl(uri.getPath());
            String[] params = uri.getQuery().split("&");
            int offset = Integer.parseInt(params[0].split("=")[1]);
            int limit = Integer.parseInt(params[1].split("=")[1]);
            for (int i = 2; i < params.length; i++) {
                String[] splittedPair = params[i].split("=");
                query.append(splittedPair[0], splittedPair[1]);
            }
            String jsonStr;
            if (query.isEmpty()) {
                jsonStr = new Gson().toJson(myServer.patientDAO.getAll(offset, limit));
            } else {
                jsonStr = new Gson().toJson(myServer.patientDAO.find(query, offset, limit));
            }
            responseCreator.doGetResponse(httpExchange, jsonStr, String.valueOf(myServer.patientDAO.getNumberRecords(query)));
        } catch (CollectionNotFoundException | NoSuchDataBaseException e) {
            myServer.myLogger.logError(e.getMessage());
            responseCreator.doErrorResponse(httpExchange, 404);
        }

    }

    private void handlePostRequest(HttpExchange httpExchange) {
        try {
            var bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            String content = bufferedReader.readLine();
            if (" ".equals(content)) {
                myServer.myLogger.logMessage("Handling POST(collection) request");
                String[] dividedPath = httpExchange.getRequestURI().getPath().split("/");
                String databaseName = dividedPath[2];
                String collectionName = dividedPath[3];
                myServer.mongoModifier = new MongoModifier(databaseName);
                myServer.mongoModifier.createCollection(collectionName);
            } else {
                myServer.myLogger.logMessage("Handling POST request");
                Document result = new Document();
                URI uri = httpExchange.getRequestURI();
                myServer.patientDAO = new PatientDAOImpl(uri.getPath());
                Gson gson = new Gson();
                Patient patient = gson.fromJson(content, Patient.class);
                myServer.patientDAO.add(patient);
            }
            responseCreator.doPostResponse(httpExchange);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CollectionNotFoundException | NoSuchDataBaseException e) {
            myServer.myLogger.logError(e.getMessage());
            responseCreator.doErrorResponse(httpExchange, 404);
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) {
        try {
            int deletedPatients = 0;
            URI uri = httpExchange.getRequestURI();
            System.out.println(uri);
            if (uri.getQuery() == null) {
                myServer.myLogger.logMessage("Handling DELETE(connection) request");
                String[] dividedPath = httpExchange.getRequestURI().getPath().split("/");
                for (int i = 0; i < 3; i++) {
                    System.out.println(i + ": " + dividedPath[i]);
                }
                myServer.myLogger.logMessage("1");
                String databaseName = dividedPath[2];
                myServer.myLogger.logMessage("2");
                String collectionName = dividedPath[3];
                myServer.myLogger.logMessage("3");
                myServer.mongoModifier = new MongoModifier(databaseName);
                myServer.myLogger.logMessage("4");
                myServer.mongoModifier.dropCollection(collectionName);
                myServer.myLogger.logMessage("5");
            } else {
                myServer.myLogger.logMessage("6");
                Document result = new Document();
                myServer.patientDAO = new PatientDAOImpl(uri.getPath());
                String[] params = uri.getQuery().split("&");
                for (var pair : params) {
                    String[] splittedPair = pair.split("=");
                    result.append(splittedPair[0], splittedPair[1]);
                }
                deletedPatients = myServer.patientDAO.deleteAll(result);
            }
            myServer.myLogger.logMessage("7");
            responseCreator.doDeleteResponse(httpExchange, String.valueOf(deletedPatients));
        } catch (CollectionNotFoundException | NoSuchDataBaseException e) {
            myServer.myLogger.logError(e.getMessage());
            responseCreator.doErrorResponse(httpExchange, 404);
        }
    }
}
