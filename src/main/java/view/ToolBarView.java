package view;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import presenter.Presenter;

public class ToolBarView extends ToolBar implements IView {
    Presenter presenter;
    Button showAllButton;
    Button addRecordButton;
    Button findButton;
    Button deleteButton;
    Button editTableButton;

    {
        showAllButton = new Button("Update");
        addRecordButton = new Button("Add");
        findButton = new Button("Find");
        deleteButton = new Button("Delete");

    }

    public ToolBarView(){
    }

    public ToolBarView(Presenter presenter){
        this.presenter = presenter;
        configureButtons(presenter);
    }

    private void configureButtons(Presenter presenter){
        this.getItems().addAll(showAllButton, addRecordButton, findButton, deleteButton);
        showAllButton.setOnAction(actionEvent -> presenter.setOnUpdateButton());
        deleteButton.setOnAction(actionEvent -> presenter.setOnDelete());
        addRecordButton.setOnAction(actionEvent -> presenter.setOnAdd());

    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        configureButtons(presenter);
    }
}
