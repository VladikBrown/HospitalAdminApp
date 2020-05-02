package presenter;

import model.IModel;

public interface IPresenter {
    IModel getModel();

    void setModel(IModel model);

    void loadView();
}
