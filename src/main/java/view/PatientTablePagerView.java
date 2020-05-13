package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.entity.Patient;
import presenter.TablePagerViewPresenter;

//уродский класс перепиши влад молю
public class PatientTablePagerView implements TablePagerView<Patient> {
    private Pagination pagination;
    private TablePagerViewPresenter<Patient> tablePagerViewPresenter;
    private int numberOfRecords;
    private int itemsPerPage;
    private int rowsPerPage;
    private boolean isAnchored;

    public PatientTablePagerView() {
        this.itemsPerPage = 1;
        this.rowsPerPage = 5;
        this.isAnchored = false;
    }

    public PatientTablePagerView(TablePagerViewPresenter<Patient> tablePagerViewPresenter) {
        this.tablePagerViewPresenter = tablePagerViewPresenter;
        this.itemsPerPage = 1;
        this.rowsPerPage = 5;
        this.isAnchored = false;
    }

    public AnchorPane anchorPager(AnchorPane parentForPager) {
        pagination = new Pagination(0, 0);
        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setLeftAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 0.0);
        AnchorPane.setBottomAnchor(pagination, 0.0);
        parentForPager.getChildren().addAll(pagination);
        this.isAnchored = true;
        return parentForPager;
    }

    @Override
    public void createPager(ObservableList<Patient> patients) {
        int numOfPages = 1;

        //TODO в отдельный метод проверку
        //здесь patients.size заменить на количество записей всего (totalRecords)
        if (patients.size() % rowsPerPage == 0) {
            numOfPages = patients.size() / rowsPerPage;
        } else if (patients.size() > rowsPerPage) {
            numOfPages = patients.size() / rowsPerPage + 1;
        }

        pagination.setPageCount(numOfPages);
        if (isAnchored()) {
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > patients.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    System.out.println("hello");
                    return createPage(pageIndex, patients);
                }
            });
        }
    }

    public void createPager(ObservableList<Patient> patients, int rowsPerPage, int itemsPerPage) {
        setItemsPerPage(itemsPerPage);
        setRowsPerPage(rowsPerPage);
        createPager(patients);
    }


    //это уродище особенно перепиши
    @Override
    public VBox createPage(int currentPageIndex, ObservableList<Patient> patients) {
        int lastPageIndex;
        this.numberOfRecords += patients.size();

        int displace = patients.size() % rowsPerPage;
        if (patients.size() % rowsPerPage == 0) {
            displace = patients.size() % rowsPerPage + 1;
        }
        if (displace > 0) {
            lastPageIndex = patients.size() / rowsPerPage;
        } else {
            lastPageIndex = patients.size() / rowsPerPage - 1;

        }

        //Если последний элемент загружаемой страницы = x > размера кэша = y, то (*1)загрузить из сервера x - y записей
        // и добавить в cache, затем повторить
        // *1 - вызвать метод uploadRecords(int offset, int limit) - он загрузит в кэш нужные записи
        // все другие запросы в базу данные связанные с поиском новой информации обновляют кэщ
        VBox box = new VBox();
        int page = currentPageIndex * itemsPerPage;
        for (int i = page; i < page + itemsPerPage; i++) {
            PatientTableView table = new PatientTableView();
            table.setTablePagerViewPresenter(tablePagerViewPresenter);

            if (lastPageIndex == currentPageIndex) {
                table.setItems(FXCollections.observableArrayList(patients.subList(currentPageIndex * rowsPerPage, currentPageIndex * rowsPerPage + displace)));
            } else {
                table.setItems(FXCollections.observableArrayList(patients.subList(currentPageIndex * rowsPerPage, currentPageIndex * rowsPerPage + rowsPerPage)));
            }

            VBox.setVgrow(table, Priority.ALWAYS);
            box.getChildren().addAll(table, tableSizeChangerFactory());
        }
        return box;
    }

    private HBox tableSizeChangerFactory() {
        HBox hBox = new HBox();
        Label label = new Label(String.valueOf(rowsPerPage));
        hBox.getChildren().addAll(decreaseTableSizeButtonFactory(), label, increaseTableSizeButtonFactory());
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Button decreaseTableSizeButtonFactory() {
        Button decreaseButton = new Button("-");
        decreaseButton.setOnAction(actionEvent -> {
            decreaseAmountOfCells();
            tablePagerViewPresenter.onAmountOfCellsChanged();
        });
        return decreaseButton;
    }

    private Button increaseTableSizeButtonFactory() {
        Button addButton = new Button("+");
        addButton.setOnAction(actionEvent -> {
            increaseAmountOfCells();
            tablePagerViewPresenter.onAmountOfCellsChanged();
        });
        return addButton;
    }

    public boolean isAnchored() {
        return isAnchored;
    }

    @Override
    public TablePagerViewPresenter getTablePagerViewPresenter() {
        return tablePagerViewPresenter;
    }

    @Override
    public void setTablePagerViewPresenter(TablePagerViewPresenter tablePagerViewPresenter) {
        this.tablePagerViewPresenter = tablePagerViewPresenter;
    }

    public void increaseAmountOfCells() {
        this.rowsPerPage++;
    }

    public void decreaseAmountOfCells() {
        if (this.rowsPerPage > 0) {
            this.rowsPerPage--;
        }
    }

    @Override
    public int getItemsPerPage() {
        return 1;
    }

    @Override
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public int getRowsPerPage() {
        return 5;
    }

    @Override
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }
}
