package mongoDAO.impl;

import mongoDAO.DataBaseModifier;
import mongoDAO.WorkWithMongo;
import server.exceptions.NoSuchDataBaseException;

public class MongoModifier implements DataBaseModifier {
    WorkWithMongo workWithMongo;

    public MongoModifier(String dataBaseName) throws NoSuchDataBaseException {
        workWithMongo = new WorkWithMongo(dataBaseName);
    }

    @Override
    public void createCollection(String collection) {
        workWithMongo.createCollection(collection);
    }

    @Override
    public void dropCollection(String collection) {
        workWithMongo.dropCollection(collection);
    }
}
