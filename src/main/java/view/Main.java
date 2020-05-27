package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.scenes.EntryWindowView;

public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        EntryWindowView entryWindowView = new EntryWindowView(primaryStage);
        Scene root = new Scene(entryWindowView.asParent(), 1000, 600);
        primaryStage.setTitle("Hospital alpha");
        root.getStylesheets().add("Theme.css");
        primaryStage.setScene(root);
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
