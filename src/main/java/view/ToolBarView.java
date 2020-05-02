package view;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import presenter.ToolBarViewPresenter;

public class ToolBarView extends ToolBar implements IToolBarView {
    ToolBarViewPresenter presenter;
    Button updateButton;
    Button addRecordButton;
    Button findButton;
    Button deleteButton;
    Button findAndDelete;

    {
        updateButton = new Button("Update");
        addRecordButton = new Button("Add");
        findButton = new Button("Find");
        deleteButton = new Button("Delete");
        findAndDelete = new Button("Find And Delete");
    }

    public ToolBarView() {
    }

    public ToolBarView(ToolBarViewPresenter presenter) {
        this.presenter = presenter;
        configureButtons(presenter);
    }

    @Override
    public void setToolBarViewPresenter(ToolBarViewPresenter toolBarViewPresenter) {
        this.presenter = toolBarViewPresenter;
        configureButtons(toolBarViewPresenter);
    }

    @Override
    public ToolBarViewPresenter getToolbarViewPresenter() {
        return presenter;
    }

    @Override
    public void configureButtons(ToolBarViewPresenter presenter) {
        this.getItems().addAll(updateButton, addRecordButton, findButton, deleteButton, findAndDelete);
        onAddButton();
        onDeleteButton();
        onFindButton();
        onFindAndDeleteButton();
        onUpdateButton();
    }

    @Override
    public void onUpdateButton() {
        updateButton.setOnAction(actionEvent -> presenter.onUpdate());
    }

    @Override
    public void onAddButton() {
        addRecordButton.setOnAction(actionEvent -> presenter.onAdd());
    }

    @Override
    public void onDeleteButton() {
        deleteButton.setOnAction(actionEvent -> presenter.onDelete());
    }

    @Override
    public void onFindButton() {
        findButton.setOnAction(actionEvent -> presenter.onFind());
    }

    @Override
    public void onFindAndDeleteButton() {
        findAndDelete.setOnAction(actionEvent -> presenter.onFindAndDelete());
    }
}
