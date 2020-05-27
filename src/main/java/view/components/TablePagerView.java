package view.components;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import presenter.TablePagerViewPresenter;

public interface TablePagerView<T> extends IView {

    TablePagerViewPresenter getTablePagerViewPresenter();

    void setTablePagerViewPresenter(TablePagerViewPresenter tablePagerViewPresenter);

    int getItemsPerPage();

    void setItemsPerPage(int itemsPerPage);

    int getRowsPerPage();

    void setRowsPerPage(int rowsPerPage);

    void createPager(ObservableList<T> entities, int totalNumberOfRecords);

    VBox createPage(int pageIndex, ObservableList<T> patients);
}
