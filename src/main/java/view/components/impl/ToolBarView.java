package view.components.impl;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import presenter.ToolBarViewPresenter;
import view.components.IToolBarView;

public class ToolBarView extends ToolBar implements IToolBarView {
    private ToolBarViewPresenter presenter;
    private final Button updateButton;
    private final Button addRecordButton;
    private final Button findButton;
    private final Button deleteButton;
    private final Button findAndDelete;
    private final Button changeCollectionButton;
    private boolean isChangeCollectionActive = false;

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

    {
        updateButton = new Button("Update");
        addRecordButton = new Button("Add");
        findButton = new Button("Find");
        deleteButton = new Button("Delete");
        findAndDelete = new Button("Find And Delete");
        changeCollectionButton = new Button("Reconnect");
    }

    @Override
    public void configureButtons(ToolBarViewPresenter presenter) {
        this.getItems().addAll(updateButton, addRecordButton, findButton, deleteButton, findAndDelete, changeCollectionButton);
        //this.getItems().forEach(node -> ((Button) node).setMinWidth(150));
        onAddButton();
        onDeleteButton();
        onFindButton();
        onFindAndDeleteButton();
        onUpdateButton();
        onChangeCollectionButton();
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

    public ObservableList<Node> getTools() {
        return getItems();
    }

    @Override
    public void onChangeCollectionButton() {
        changeCollectionButton.setOnAction(actionEvent -> {
            if (!isChangeCollectionActive) {
                changeCollectionButton.setText("Confirm");
            } else changeCollectionButton.setText("Reconnect");
            presenter.onChangeCollection();
            isChangeCollectionActive = !isChangeCollectionActive;
        });
    }
}
