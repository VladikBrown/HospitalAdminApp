package view.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EntryWindowView {
    private static final String APPLICATION_NAME = "HospitalApp";
    private final Stage primaryStage;
    //containers
    private final BorderPane borderPane;
    private final GridPane gridPane = new GridPane();
    private final AnchorPane buttonAnchor = new AnchorPane();
    private final AnchorPane gridPaneAnchor = new AnchorPane();
    private final AnchorPane labelAnchor = new AnchorPane();

    //controls
    private final Label titleLabel = new Label("Welcome to the " + APPLICATION_NAME);
    private final Label hostLabel = new Label("Host and Port: ");
    private final Label databasePathLabel = new Label("Database name: ");
    private final Label collectionNameLabel = new Label("Collection name: ");

    private final TextField hostInput = new TextField("localhost:8080");
    private final TextField dataBasePathInput = new TextField("/hospitalApp/hospitalDB/");
    private final TextField collectionNameInput = new TextField("patients");

    private final Button enterButton = new Button("Confirm");

    //constraints
    ColumnConstraints
            columnConstraints = new ColumnConstraints(0, 0, Double.MAX_VALUE),
            columnConstraints1 = new ColumnConstraints(0, 0, Double.MAX_VALUE);
    RowConstraints
            rowConstraints = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints1 = new RowConstraints(0, 0, Double.MAX_VALUE),
            rowConstraints2 = new RowConstraints(0, 0, Double.MAX_VALUE);

    public EntryWindowView(Stage primaryStage) {
        borderPane = new BorderPane();
        labelAnchor.setStyle("-fx-background-color: #3a3a3a");
        gridPaneAnchor.setStyle("-fx-background-color: #1d1d1d");
        buttonAnchor.setStyle("-fx-background-color: #3a3a3a");
        gridPane.setStyle("-fx-background-color: #1d1d1d");
        this.primaryStage = primaryStage;
        configureTitle();
        configureGridPane();
        configureGridConstraints();
        configureButton();
    }

    private void configureTitle() {
        borderPane.setTop(labelAnchor);
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 0.0);
        labelAnchor.getChildren().addAll(titleLabel);
        titleLabel.setStyle("-fx-font-size: 18");
    }

    private void configureGridPane() {
        borderPane.setCenter(gridPaneAnchor);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 150.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setTopAnchor(gridPane, 0.0);
        gridPaneAnchor.getChildren().addAll(gridPane);
    }

    private void configureGridConstraints() {
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints1.setHgrow(Priority.ALWAYS);
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints1.setVgrow(Priority.ALWAYS);
        rowConstraints2.setVgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints1);
        gridPane.getRowConstraints().addAll(rowConstraints, rowConstraints1, rowConstraints2);

        GridPane.setConstraints(hostLabel, 0, 0);
        GridPane.setConstraints(hostInput, 1, 0);
        GridPane.setConstraints(databasePathLabel, 0, 1);
        GridPane.setConstraints(dataBasePathInput, 1, 1);
        GridPane.setConstraints(collectionNameLabel, 0, 2);
        GridPane.setConstraints(collectionNameInput, 1, 2);
        gridPane.getChildren().addAll(hostLabel, hostInput, databasePathLabel, dataBasePathInput, collectionNameLabel,
                collectionNameInput);
    }

    private void configureButton() {
        borderPane.setBottom(buttonAnchor);
        enterButton.setPrefHeight(50);
        AnchorPane.setLeftAnchor(enterButton, 240.0);
        AnchorPane.setRightAnchor(enterButton, 240.0);
        buttonAnchor.getChildren().addAll(enterButton);
        enterButton.setOnAction(actionEvent -> {
            try {
                Properties properties = new Properties();
                var fis = new FileInputStream("src/main/resources/config.properties");
                properties.load(fis);
                properties.put("server.host", hostInput.getText());
                properties.put("server.root", dataBasePathInput.getText());
                properties.put("db.collection", collectionNameInput.getText());
                properties.store(new FileOutputStream("src/main/resources/config.properties"), "entry properties");
                Scene mainWindow = new Scene(new MainWindowView(primaryStage).asParent(), 1000, 600);
                mainWindow.getStylesheets().add("Theme.css");
                primaryStage.setScene(mainWindow);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Parent asParent() {
        return borderPane;
    }
}
