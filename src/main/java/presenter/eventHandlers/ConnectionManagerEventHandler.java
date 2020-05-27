package presenter.eventHandlers;

public interface ConnectionManagerEventHandler {
    void onCreate(String collection);

    void onDelete(String collection);
}
