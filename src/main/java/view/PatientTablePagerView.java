package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.*;
import model.Patient;
import presenter.Presenter;

public class PatientTablePagerView implements IView {
    private Pagination pagination;
    private Presenter presenter;
    private int itemsPerPage;
    private int rowsPerPage;
    private boolean isAnchored;

    public PatientTablePagerView(){
        this.itemsPerPage = 1;
        this.rowsPerPage = 5;
        this.isAnchored = false;
    }

    public PatientTablePagerView(Presenter presenter){
        this.presenter = presenter;
        this.itemsPerPage = 1;
        this.rowsPerPage = 5;
        this.isAnchored = false;
    }

    public AnchorPane anchorPager(AnchorPane parentForPager){
        pagination = new Pagination(0, 0);
        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setLeftAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 0.0);
        AnchorPane.setBottomAnchor(pagination, 0.0);
        parentForPager.getChildren().addAll(pagination);
        this.isAnchored = true;
        return parentForPager;
    }

    public void createPager(ObservableList<Patient> patients){
        int numOfPages = 1;
        if (patients.size() % rowsPerPage == 0) {
            numOfPages = patients.size() / rowsPerPage;
        } else if (patients.size() > rowsPerPage) {
            numOfPages = patients.size() / rowsPerPage + 1;
        }
        pagination.setPageCount(numOfPages);
        if(isAnchored()){
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > patients.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex, patients);
                }
            });
        }
    }

    public void createPager(ObservableList<Patient> patients, int rowsPerPage, int itemsPerPage){
        setItemsPerPage(itemsPerPage);
        setRowsPerPage(rowsPerPage);
        createPager(patients);
    }

    public VBox createPage(int pageIndex, ObservableList<Patient> patients) {
        int lastIndex = 0;
        int displace = patients.size() % rowsPerPage;
        if (patients.size() % rowsPerPage == 0) {
            displace = patients.size() % rowsPerPage + 1;
        }
        if (displace > 0) {
            lastIndex = patients.size() / rowsPerPage;
        } else {
            lastIndex = patients.size() / rowsPerPage - 1;

        }

        VBox box = new VBox();
        int page = pageIndex * itemsPerPage;

        for (int i = page; i < page + itemsPerPage; i++) {
            PatientTableView table = new PatientTableView();
            table.setPresenter(presenter);

            if (lastIndex == pageIndex) {
                table.setItems(FXCollections.observableArrayList(patients.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + displace)));
            } else {
                table.setItems(FXCollections.observableArrayList(patients.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + rowsPerPage)));
            }
            HBox hBox = new HBox();
            Button takeButton = new Button("-");
            takeButton.setOnAction(actionEvent -> {
                decreaseAmountOfCells();
                presenter.setOnUpdateButton();
            });
            Label label = new Label(String.valueOf(rowsPerPage));
            Button addButton = new Button("+");
            addButton.setOnAction(actionEvent -> {
                increaseAmountOfCells();
                presenter.setOnUpdateButton();
            });
            hBox.getChildren().addAll(takeButton, label, addButton);
            hBox.setAlignment(Pos.CENTER);
            VBox.setVgrow(table, Priority.ALWAYS);
            box.getChildren().addAll(table, hBox);
        }
        return box;
    }

    public boolean isAnchored(){
        return isAnchored;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public void increaseAmountOfCells(){
        rowsPerPage++;
    }

    public void decreaseAmountOfCells(){
        if(rowsPerPage > 0){
            rowsPerPage--;
        }
    }

    public int getItemsPerPage() {
        return 1;
    }

    public int getRowsPerPage() {
        return 5;
    }
}
