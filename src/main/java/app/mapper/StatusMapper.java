package app.mapper;

import app.dto.StatusDto;
import app.model.Status;
import app.model.Color;

public class StatusMapper {

    public static StatusDto toDTO(Status status) {
        return new StatusDto(
                status.getId(),
                status.getName(),
                status.getDescription(),
                status.getColor() != null ? status.getColor().getValue() : null
        );
    }

    public static Status toEntity(StatusDto dto) {
        Status status = new Status(
                dto.getName(),
                dto.getDescription()
        );
        if (dto.getColor() != null) {
            status.setColor(Color.fromValue(dto.getColor()));
        }
        return status;
    }
}
