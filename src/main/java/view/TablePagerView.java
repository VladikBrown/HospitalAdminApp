package view;

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

    void createPager(ObservableList<T> patients);

    VBox createPage(int pageIndex, ObservableList<T> patients);
}
