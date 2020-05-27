package view.components.impl;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import presenter.eventHandlers.ConnectionManagerEventHandler;
import view.components.IView;

public class ConnectionManagerView implements IView {
    private final ConnectionManagerEventHandler connectionManagerPresenter;

    private MenuBar menuBar;
    private TextField inputCollectionField;
    private MenuItem menuItemCreate;
    private MenuItem menuItemDelete;
    private Menu menu;

    public ConnectionManagerView(ConnectionManagerEventHandler connectionManagerPresenter) {
        this.connectionManagerPresenter = connectionManagerPresenter;
        configureItems();
        configureActionEvents();
    }

    public void configureItems() {
        inputCollectionField = new TextField("");
        menuItemCreate = new MenuItem("Create");
        menuItemDelete = new MenuItem("Delete");
        menu = new Menu("Create", inputCollectionField, menuItemCreate, menuItemDelete);
        menuBar = new MenuBar(menu);
    }

    public void configureActionEvents() {
        menuItemCreate.setOnAction(actionEvent -> {
            if (!inputCollectionField.getText().equals("")) {
                connectionManagerPresenter.onCreate(inputCollectionField.getText());
            }
        });

        menuItemDelete.setOnAction(actionEvent -> {
            if (!inputCollectionField.getText().equals("")) {
                connectionManagerPresenter.onDelete(inputCollectionField.getText());
            }
        });
    }

    public String getText() {
        return inputCollectionField.getText();
    }

    public MenuBar asNode() {
        return this.menuBar;
    }
}
