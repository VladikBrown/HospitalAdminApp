package client;

import java.util.List;

public interface Cacheable<T> {
    List<? extends T> getCache();

    void setCache(List<? extends T> cache);

    void updateCache(List<? extends T> cache);

    void dropCache();
}
