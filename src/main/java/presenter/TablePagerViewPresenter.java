package presenter;

import javafx.collections.ObservableList;
import view.PatientTableView;

public interface TablePagerViewPresenter<T> extends IPresenter {
    void onAmountOfCellsChanged();

    void showTable(ObservableList<T> items);

    void setOnItemSelected(T selectedItem);

    void setCurrentPage(PatientTableView patientTableView);

    boolean updateCache(int offset, int limit);
}
