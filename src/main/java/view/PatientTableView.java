package view;

import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;
import model.WorkWithMongo2;
import presenter.Presenter;

public class PatientTableView extends TableView<Patient> implements IView {
    Presenter presenter;
    Pagination pagination;

    TableColumn<Patient, String> surnameColumn;
    TableColumn<Patient, String> firstNameColumn;
    TableColumn<Patient, String> secondNameColumn;



    public PatientTableView(){

        createColumns();
    }

    public PatientTableView(Presenter presenter, Pagination pagination){
        this.presenter = presenter;
        this.pagination = pagination;

        WorkWithMongo2 workWithMongo = new WorkWithMongo2();

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
        this.getColumns().addAll(surnameColumn,firstNameColumn,secondNameColumn);
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        setOnItemSelected(presenter);
    }

    private void setOnItemSelected(Presenter presenter){
        getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->{
                    presenter.setOnPatientSelected(newValue);
                    presenter.setCurrentPage(this);
                });
    }

    //TODO: добавление страничек с пациентами
}
