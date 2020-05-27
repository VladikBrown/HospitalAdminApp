package view.components;

import presenter.TablePagerViewPresenter;

public interface ITableView extends IView {
    void setOnItemSelected();

    TablePagerViewPresenter getTablePagerViewPresenter();

    void setTablePagerViewPresenter(TablePagerViewPresenter tableViewPresenter);
}
