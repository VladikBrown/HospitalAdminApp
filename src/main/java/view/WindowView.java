package view;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import presenter.PatientPresenter;

public class WindowView {
    private final PatientPresenter patientPresenter;
    //components
    private final BorderPane view;
    private final AnchorPane splitAnchor;
    private final SplitPane centerPane;
    private final AnchorPane rightAnchor;
    private final AnchorPane leftAnchor;
    //topBorder
    private final ToolBarView topBar;
    //leftBorder - list of patients
    private final PatientTablePagerView tableView;
    //rightBorder - info about patients
    private final PatientInfoView infoPane;

    {
        view = new BorderPane();
        leftAnchor = new AnchorPane();
        rightAnchor = new AnchorPane();
        splitAnchor = new AnchorPane();
        centerPane = new SplitPane();
        topBar = new ToolBarView();
        infoPane = new PatientInfoView();
        tableView = new PatientTablePagerView();
        patientPresenter = new PatientPresenter(tableView, infoPane, topBar);
        tableView.setTablePagerViewPresenter(patientPresenter);
        topBar.setToolBarViewPresenter(patientPresenter);
        infoPane.setInfoViewPresenter(patientPresenter);
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
        splitAnchor.getChildren().addAll(centerPane);
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
