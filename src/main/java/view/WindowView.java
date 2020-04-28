package view;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import presenter.Presenter;

public class WindowView {
    Presenter presenter;
    //components
    BorderPane view;
    AnchorPane splitAnchor;
    SplitPane centerPane;
    AnchorPane rightAnchor;
    AnchorPane leftAnchor;
    //topBorder
    ToolBarView topBar;
    //leftBorder - list of patients
    Pagination pagination;
    PatientTablePagerView tableView;
    //rightBorder - info about patients
    PatientInfoView infoPane;
    {
        view = new BorderPane();
        leftAnchor  = new AnchorPane();
        rightAnchor = new AnchorPane();
        splitAnchor = new AnchorPane();
        centerPane = new SplitPane();
        pagination = new Pagination();
        topBar = new ToolBarView();
        infoPane = new PatientInfoView();
        tableView = new PatientTablePagerView();
        presenter = new Presenter(tableView, infoPane, topBar);
        tableView.setPresenter(presenter);
        topBar.setPresenter(presenter);
        infoPane.setPresenter(presenter);
    }


    WindowView(Stage primaryStage){
        configurePane();
    }

    private void configureBorder(){
        view.setTop(topBar);
        view.setCenter(splitAnchor);
        centerPane.setDividerPosition(0, 0);
        centerPane.setOrientation(Orientation.HORIZONTAL);
        AnchorPane.setTopAnchor(centerPane, 0.0);
        AnchorPane.setLeftAnchor(centerPane, 0.0);
        AnchorPane.setBottomAnchor(centerPane, 0.0);
        AnchorPane.setRightAnchor(centerPane, 0.0);
        splitAnchor.getChildren().add(centerPane);
        centerPane.getItems().add(0, leftAnchor);
        centerPane.getItems().add(1, rightAnchor);
    }

    private void configureTable(){
        //leftAnchor
        tableView.anchorPager(leftAnchor);
    }

    private void configureInfoGrid(){
        //rightAnchor
        infoPane.setHgap(10);
        infoPane.setVgap(10);
        infoPane.setMinWidth(300);
        GridPane.setHgrow(infoPane, Priority.ALWAYS);
        GridPane.setVgrow(infoPane, Priority.ALWAYS);
        AnchorPane.setTopAnchor(infoPane, 0.0);
        AnchorPane.setLeftAnchor(infoPane, 0.0);
        AnchorPane.setBottomAnchor(infoPane, 0.0);
        AnchorPane.setRightAnchor(infoPane, 0.0);
        rightAnchor.getChildren().addAll(infoPane);
    }


    private void configurePane(){
        configureBorder();
        configureTable();
        configureInfoGrid();
    }

    public Parent asParent(){
        return view;
    }
}
