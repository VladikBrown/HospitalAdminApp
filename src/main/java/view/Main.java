package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hospital alpha");
        WindowView windowView = new WindowView(primaryStage);
        Scene root = new Scene(windowView.asParent(), 1000, 600);
        root.getStylesheets().add("Theme.css");
        primaryStage.setScene(root);
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
