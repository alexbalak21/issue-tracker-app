package app.mapper;

import app.dto.StatusDto;
import app.model.Status;

public class StatusMapper {

    public static StatusDto toDTO(Status status) {
        return new StatusDto(
                status.getId(),
                status.getName(),
                status.getType()
        );
    }

    public static Status toEntity(StatusDto dto) {
        return new Status(
                dto.getName(),
                dto.getType()
        );
    }
}
