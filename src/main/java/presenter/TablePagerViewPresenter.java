package presenter;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.CacheableModel;
import view.components.ITableView;

public interface TablePagerViewPresenter<T> extends IPresenter {
    @Override
    CacheableModel<T> getModel();

    void onAmountOfCellsChanged();

    void showTable(ObservableList<T> items, int totalAmountOfPatients);

    T getSelectedItem();

    void setOnItemSelected(T selectedItem);

    void setSelectedItem(T item);

    TableView<T> getCurrentPage();

    void setCurrentPage(ITableView tableView);

    boolean updateCache(int offset, int limit);

    int getRowsPerPage();

    void setRowsPerPage(int rowsPerPage);
}
