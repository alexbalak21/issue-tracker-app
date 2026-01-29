package app.controller.admin;

import app.dto.PriorityDto;
import app.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/priorities")
public class AdminPriorityController {

	@Autowired
	private PriorityService priorityService;

	@GetMapping
	public List<PriorityDto> getAllPriorities() {
		return priorityService.getAllPriorities();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PriorityDto> getPriorityById(@PathVariable int id) {
		PriorityDto dto = priorityService.getPriorityById(id);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<PriorityDto> createPriority(@RequestBody PriorityDto dto) {
		PriorityDto created = priorityService.createPriority(dto);
		return ResponseEntity.ok(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PriorityDto> updatePriority(@PathVariable int id, @RequestBody PriorityDto dto) {
		PriorityDto updated = priorityService.updatePriority(id, dto);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePriority(@PathVariable int id) {
		boolean deleted = priorityService.deletePriority(id);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
