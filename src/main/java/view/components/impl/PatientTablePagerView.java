package view.components.impl;

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
import view.components.TablePagerView;

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
    public void createPager(ObservableList<Patient> cache, int totalNumberOfRecords) {
        int numOfPages = 1;

        //TODO в отдельный метод проверку
        if (totalNumberOfRecords % rowsPerPage == 0) {
            numOfPages = totalNumberOfRecords / rowsPerPage;
        } else if (totalNumberOfRecords > rowsPerPage) {
            numOfPages = totalNumberOfRecords / rowsPerPage + 1;
        }

        pagination.setPageCount(numOfPages);
        if (isAnchored()) {
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > totalNumberOfRecords / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex, cache);
                }
            });
        }
    }


    //это уродище особенно перепиши
    @Override
    public VBox createPage(int currentPageIndex, ObservableList<Patient> cache) {
        int lastPageIndex;
        this.numberOfRecords += cache.size();

        int displace = cache.size() % rowsPerPage;
        if (cache.size() % rowsPerPage == 0) {
            displace = cache.size() % rowsPerPage + 1;
        }

        if (displace > 0) {
            lastPageIndex = tablePagerViewPresenter.getModel().getCurrentTotalNumberOfRecords() / rowsPerPage;
        } else {
            lastPageIndex = tablePagerViewPresenter.getModel().getCurrentTotalNumberOfRecords() / rowsPerPage - 1;
        }

        if (((currentPageIndex + 1) * rowsPerPage) > cache.size()) {
            System.out.println("bef: " + cache.size());
            getTablePagerViewPresenter().updateCache(cache.size(), rowsPerPage);
            System.out.println("aft: " + cache.size());
        }

        VBox box = new VBox();
        int page = currentPageIndex * itemsPerPage;
        for (int i = page; i < page + itemsPerPage; i++) {
            PatientTableView table = new PatientTableView();
            table.setTablePagerViewPresenter(tablePagerViewPresenter);

            if (!cache.isEmpty()) {
                if (lastPageIndex == currentPageIndex) {
                    table.setItems(FXCollections.observableArrayList(cache.subList(currentPageIndex * rowsPerPage, currentPageIndex * rowsPerPage + displace)));
                } else {
                    table.setItems(FXCollections.observableArrayList(cache.subList(currentPageIndex * rowsPerPage, currentPageIndex * rowsPerPage + rowsPerPage)));
                }
            } else {
                table.setItems(cache);
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
