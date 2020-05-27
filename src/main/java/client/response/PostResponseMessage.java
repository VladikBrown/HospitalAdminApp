package client.response;

public class PostResponseMessage implements IMessage {
    private final String statusCode;
    private final String reasonPhrase;

    public PostResponseMessage(String errorCode, String reasonPhrase) {
        this.statusCode = errorCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public boolean isSuccessful() {
        return !(this.statusCode.startsWith("4") || this.statusCode.startsWith("5"));
    }
}
