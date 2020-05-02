package presenter;

import view.InfoView;

public interface InfoViewPresenter<T> extends IPresenter {
    InfoView<T> getInfoView();

    void setInfoView(InfoView<T> infoView);

    void showSelected(T object);
}
