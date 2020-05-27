package mongoDAO;

public interface DataBaseModifier {

    void createCollection(String collection);

    void dropCollection(String collection);
}
