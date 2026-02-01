package app.controller;

import app.dto.NoteCreateRequest;
import app.dto.NoteUpdateRequest;
import app.service.NoteService;
import app.service.AuthService;
import app.security.RequiresPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AuthService authService;

    @GetMapping("/{ticketId}")
    @RequiresPermission("note.read")
    public ResponseEntity<?> getNotes(@PathVariable Long ticketId) {
        return ResponseEntity.ok(noteService.getNotes(ticketId));
    }

    @PostMapping
    @RequiresPermission("note.create")
    public ResponseEntity<?> createNote(@RequestBody NoteCreateRequest req) {
        Long userId = authService.getCurrentUserId();
        return ResponseEntity.ok(noteService.createNote(userId, req));
    }

    @PutMapping("/{noteId}")
    @RequiresPermission({ "note.create", "note.manage" })
    public ResponseEntity<?> updateNote(
            @PathVariable Long noteId,
            @RequestBody NoteUpdateRequest req) {
        Long userId = authService.getCurrentUserId();
        return ResponseEntity.ok(noteService.updateNote(userId, noteId, req));
    }

    @DeleteMapping("/{noteId}")
    @RequiresPermission({ "note.create", "note.manage" })
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        Long userId = authService.getCurrentUserId();
        noteService.deleteNote(userId, noteId);
        return ResponseEntity.noContent().build();
    }
}
