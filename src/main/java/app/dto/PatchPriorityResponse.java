package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatchPriorityResponse {

    @JsonProperty("priority_id")
    private final Integer priorityId;

    public PatchPriorityResponse(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Integer getPriorityId() {
        return priorityId;
    }
}
