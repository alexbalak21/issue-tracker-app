package app.controller;

import app.dto.StatusDto;
import app.mapper.StatusMapper;
import app.model.Status;
import app.security.RequiresPermission;
import app.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    @RequiresPermission({"status.read", "status.manage"})
    public List<StatusDto> getAll() {
        return statusService.findAll()
                .stream()
                .map(StatusMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @RequiresPermission({"status.read", "status.manage"})
    public ResponseEntity<StatusDto> getById(@PathVariable int id) {
        return statusService.findById(id)
                .map(StatusMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @RequiresPermission({"status.write", "status.manage"})
    public ResponseEntity<StatusDto> create(@RequestBody StatusDto dto) {
        Status saved = statusService.save(StatusMapper.toEntity(dto));
        return ResponseEntity.ok(StatusMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    @RequiresPermission({"status.write", "status.manage"})
    public ResponseEntity<StatusDto> update(@PathVariable int id, @RequestBody StatusDto dto) {
        return statusService.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setType(dto.getType());
                    Status updated = statusService.save(existing);
                    return ResponseEntity.ok(StatusMapper.toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @RequiresPermission({"status.delete", "status.manage"})
    public ResponseEntity<Void> delete(@PathVariable int id) {
        statusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
