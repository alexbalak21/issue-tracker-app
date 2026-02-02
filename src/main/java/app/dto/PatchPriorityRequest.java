package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class PatchPriorityRequest {

    @JsonProperty("priority_id")
    @NotNull(message = "priority_id is required")
    private Integer priorityId;

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }
}
