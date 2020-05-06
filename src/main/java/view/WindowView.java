package view;

import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import presenter.Presenter;

public class WindowView {
    private Presenter presenter;
    //components
    private BorderPane view;
    private AnchorPane splitAnchor;
    private SplitPane centerPane;
    private AnchorPane rightAnchor;
    private AnchorPane leftAnchor;
    //topBorder
    private ToolBarView topBar;
    //leftBorder - list of patients
    private PatientTablePagerView tableView;
    //rightBorder - info about patients
    private PatientInfoView infoPane;

    {
        view = new BorderPane();
        leftAnchor = new AnchorPane();
        rightAnchor = new AnchorPane();
        splitAnchor = new AnchorPane();
        centerPane = new SplitPane();
        topBar = new ToolBarView();
        infoPane = new PatientInfoView();
        tableView = new PatientTablePagerView();
        presenter = new Presenter(tableView, infoPane, topBar);
        tableView.setTablePagerViewPresenter(presenter);
        topBar.setToolBarViewPresenter(presenter);
        infoPane.setInfoViewPresenter(presenter);
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
