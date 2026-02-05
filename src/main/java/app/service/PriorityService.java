package app.service;

import app.dto.PriorityDto;
import app.model.Priority;
import app.repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriorityService {
    @Autowired
    private PriorityRepository priorityRepository;

    public List<PriorityDto> getAllPriorities() {
        return priorityRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PriorityDto getPriorityById(int id) {
        Optional<Priority> priority = priorityRepository.findById(id);
        return priority.map(this::toDto).orElse(null);
    }

    public PriorityDto createPriority(PriorityDto dto) {
        Priority priority = new Priority(dto.getName(), dto.getLevel(), dto.getDescription(), dto.getColor());
        Priority saved = priorityRepository.save(priority);
        return toDto(saved);
    }

    public PriorityDto updatePriority(int id, PriorityDto dto) {
        Optional<Priority> optionalPriority = priorityRepository.findById(id);
        if (optionalPriority.isPresent()) {
            Priority priority = optionalPriority.get();
            priority.setName(dto.getName());
            priority.setLevel(dto.getLevel());
            priority.setDescription(dto.getDescription());
            priority.setColor(dto.getColor());
            Priority updated = priorityRepository.save(priority);
            return toDto(updated);
        }
        return null;
    }

    public boolean deletePriority(int id) {
        if (priorityRepository.existsById(id)) {
            priorityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PriorityDto toDto(Priority priority) {
        return new PriorityDto(priority.getId(), priority.getName(), priority.getLevel(), priority.getDescription(), priority.getColor());
    }
}
