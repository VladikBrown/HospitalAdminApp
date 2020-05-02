package view;

import presenter.ToolBarViewPresenter;

public interface IToolBarView extends IView {
    void setToolBarViewPresenter(ToolBarViewPresenter toolBarViewPresenter);

    ToolBarViewPresenter getToolbarViewPresenter();

    void configureButtons(ToolBarViewPresenter presenter);

    void onUpdateButton();

    void onAddButton();

    void onDeleteButton();

    void onFindButton();

    void onFindAndDeleteButton();
}
