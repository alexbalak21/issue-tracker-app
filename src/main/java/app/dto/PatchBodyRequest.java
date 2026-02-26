package app.dto;

public class PatchBodyRequest {
    private String body;

    public PatchBodyRequest() {}

    public PatchBodyRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
