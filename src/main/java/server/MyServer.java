package server;

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
import mongoDAO.impl.MongoModifier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


//TODO придумать архитектуру
public class MyServer extends Application {
    PatientDAO patientDAO;
    MongoModifier mongoModifier;
    HttpServer httpServer;
    MyLogger myLogger;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TextArea textArea = new TextArea();
        myLogger = new MyLogger(textArea);
        stage.setTitle("Hospital Server");
        Button connectButton = new Button("connect");
        connectButton.setPrefSize(200, 80);
        connectButton.setOnAction(actionEvent -> connection());
        Button disconnectButton = new Button("disconnect");
        //nullable
        disconnectButton.setOnAction(actionEvent -> httpServer.stop(3));
        disconnectButton.setPrefSize(200, 80);
        disconnectButton.setOnAction(actionEvent -> {
            httpServer.stop(0);
            myLogger.logMessage("server stopped");
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
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            httpServer.createContext("/hospitalApp", new RequestHandler(this));
            httpServer.setExecutor(threadPoolExecutor);
            httpServer.start();
            myLogger.logMessage("Connection to port: " + 8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO класс с log-фразами
}
