package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchStatusResponse {

    @JsonProperty("status_id")
    private Integer statusId;

    public PatchStatusResponse(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
