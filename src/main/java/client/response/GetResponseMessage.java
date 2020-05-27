package client.response;

public class GetResponseMessage implements IMessage {
    private final String statusCode;
    private final String reasonPhrase;
    private String jsonEntity;
    private String numberOfRecords;

    public GetResponseMessage(String errorCode, String reasonPhrase) {
        this.statusCode = errorCode;
        this.reasonPhrase = reasonPhrase;
    }

    public GetResponseMessage(String statusCode, String reasonPhrase, String jsonEntity, String numberOfRecords) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.jsonEntity = jsonEntity;
        this.numberOfRecords = numberOfRecords;
    }

    @Override
    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String getJsonEntity() {
        return jsonEntity;
    }

    public void setJsonEntity(String jsonEntity) {
        this.jsonEntity = jsonEntity;
    }

    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    @Override
    public boolean isSuccessful() {
        return !(this.statusCode.startsWith("4") || this.statusCode.startsWith("5"));
    }
}
