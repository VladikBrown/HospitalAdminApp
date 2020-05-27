package model;

import client.Cacheable;

public interface CacheableModel<T> extends Cacheable<T>, IModel<T> {

    String getCurrentState();

    void setCurrentState(String state);

    int getCurrentTotalNumberOfRecords();

    void setCurrentTotalNumberOfRecords(int currentTotalNumberOfRecords);
}
