package view.components.impl;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entity.Patient;
import presenter.TablePagerViewPresenter;
import view.components.ITableView;

public class PatientTableView extends TableView<Patient> implements ITableView {
    private TablePagerViewPresenter<Patient> presenter;

    private TableColumn<Patient, String> surnameColumn;
    private TableColumn<Patient, String> firstNameColumn;
    private TableColumn<Patient, String> secondNameColumn;

    public PatientTableView() {
        createColumns();
    }

    public PatientTableView(TablePagerViewPresenter presenter) {
        this.presenter = presenter;
        createColumns();
    }

    private void createColumns(){
        setFixedCellSize(20);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setMinWidth(300);
        surnameColumn = new TableColumn<>("Surname");
        firstNameColumn = new TableColumn<>("Name");
        secondNameColumn = new TableColumn<>("Second name");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        this.getColumns().addAll(surnameColumn, firstNameColumn, secondNameColumn);
    }

    @Override
    public void setOnItemSelected() {
        getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.presenter.setOnItemSelected(newValue);
                    this.presenter.setCurrentPage(this);
                });
    }

    @Override
    public TablePagerViewPresenter getTablePagerViewPresenter() {
        return this.presenter;
    }

    @Override
    public void setTablePagerViewPresenter(TablePagerViewPresenter tableViewPresenter) {
        this.presenter = tableViewPresenter;
        setOnItemSelected();
    }
}
