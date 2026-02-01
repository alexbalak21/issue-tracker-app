package app.dto;

public class AddMessageRequest {
    private String message;

    public AddMessageRequest() {}

    public AddMessageRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
