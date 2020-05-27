package client.response;

public class DeleteResponseMessage implements IMessage {
    private final String statusCode;
    private final String reasonPhrase;
    private int numberOfDeletedRecords;

    public DeleteResponseMessage(String statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public DeleteResponseMessage(String statusCode, String reasonPhrase, int numberOfDeletedRecords) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.numberOfDeletedRecords = numberOfDeletedRecords;
    }

    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public int getNumberOfDeletedRecords() {
        return numberOfDeletedRecords;
    }

    @Override
    public boolean isSuccessful() {
        return !this.statusCode.startsWith("4") || !this.statusCode.startsWith("5");
    }
}
