package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mongoDAO.PatientDAO;
import mongoDAO.impl.PatientDAOImpl;
import org.bson.Document;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;

//TODO logger
//TODO constants
//TODO продумать архитектуру
public class MyServer extends Application {
    PatientDAO patientDAO;
    HttpServer httpServer;
    MyLogger myLogger;


    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE - Integer.MIN_VALUE);
        launch(args);

    }

    @Override
    public void start(Stage stage) {
        TextArea textArea = new TextArea();
        myLogger = new MyLogger(textArea);
        stage.setTitle("Hospital alpha");
        Button connectButton = new Button("connect");
        connectButton.setPrefSize(200, 80);
        connectButton.setOnAction(actionEvent -> connection());
        Button disconnectButton = new Button("disconnect");
        //nullable
        disconnectButton.setOnAction(actionEvent -> httpServer.stop(3));
        disconnectButton.setPrefSize(200, 80);
        disconnectButton.setOnAction(actionEvent -> {
        });
        VBox vBox = new VBox();
        AnchorPane anchorTextArea = new AnchorPane(textArea);
        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setBottomAnchor(textArea, 0.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);
        FlowPane buttonSection = new FlowPane();
        buttonSection.getChildren().addAll(connectButton, disconnectButton);
        VBox.setVgrow(anchorTextArea, Priority.ALWAYS);
        VBox.setVgrow(buttonSection, Priority.NEVER);
        vBox.getChildren().addAll(anchorTextArea, buttonSection);

        Scene root = new Scene(vBox, 400, 600);
        stage.setScene(root);
        stage.setResizable(false);
        stage.show();
    }

    public void connection() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            myLogger.logMessage("Connection created!");
            httpServer.createContext("/hospitalApp", new RequestHandler());
            httpServer.start();
            myLogger.logMessage("Connection to port: " + 8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try (var serverSocket = new ServerSocket(8080);
             var clientSocket = serverSocket.accept();
             var bufferReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             var bufferWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            System.out.print("Connection accepted.");
            // канал записи в сокет
            System.out.println("DataInputStream created");
            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            while (!clientSocket.isClosed()) {
                System.out.println("Server reading from channel");
                String entry = bufferReader.readLine();

                System.out.println("READ from client message - " + entry);

                System.out.println("Server try writing to channel");

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connections suicide ...");
                    bufferWriter.write("Server reply - " + entry + " - OK" + "\n");
                    bufferWriter.flush();
                    break;
                }

                bufferWriter.write("Server reply - " + entry + " - OK" + "\n");
                System.out.println("Server Wrote message to client.");

                bufferWriter.flush();
            }
            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            myLogger.logMessage("Received from client:\n" + exchange.getRequestMethod() + exchange.getRequestURI());
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGetRequest(exchange);
                case "POST" -> handlePostRequest(exchange);
                case "DELETE" -> handleDeleteRequest(exchange);
            }
        }

        private void handleGetRequest(HttpExchange httpExchange) {
            myLogger.logMessage("Handling GET request");
            Document result = new Document();
            URI uri = httpExchange.getRequestURI();
            patientDAO = new PatientDAOImpl(uri.getPath());
            String[] params = uri.getQuery().split("&");
            for (var pair : params) {
                String[] splittedPair = pair.split("=");
                result.append(splittedPair[0], splittedPair[1]);
            }
            String jsonStr = new Gson().toJson(MyServer.this.patientDAO.find(result));
            handleResponse(httpExchange, jsonStr);
        }

        private void handlePostRequest(HttpExchange httpExchange) {

        }

        private void handleDeleteRequest(HttpExchange httpExchange) {
            myLogger.logMessage("Handling DELETE request");
            Document result = new Document();
            URI uri = httpExchange.getRequestURI();
            patientDAO = new PatientDAOImpl(uri.getPath());
            String[] params = uri.getQuery().split("&");
            for (var pair : params) {
                String[] splittedPair = pair.split("=");
                result.append(splittedPair[0], splittedPair[1]);
            }
            int deletedPatients = MyServer.this.patientDAO.deleteAll(result);
            handleResponse(httpExchange, String.valueOf(deletedPatients));
        }

        void handleResponse(HttpExchange httpExchange, String response) {
            myLogger.logMessage("Handling response");
            OutputStream outputStream = httpExchange.getResponseBody();
            try {
                httpExchange.sendResponseHeaders(200, response.length());
                outputStream.write(response.getBytes());
                outputStream.flush();
                outputStream.close();
                myLogger.logMessage("Response sanded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
