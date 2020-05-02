package view;

import presenter.InfoViewPresenter;

public interface InfoView<T> extends IView {
    void showInfo(T entity);

    InfoViewPresenter getInfoViewPresenter();

    void setInfoViewPresenter(InfoViewPresenter infoViewPresenter);
}
