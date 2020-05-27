package client.response;

public interface IMessage {
    String getStatusCode();

    String getReasonPhrase();

    boolean isSuccessful();
}
