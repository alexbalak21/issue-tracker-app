package app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import app.dto.NoteCreateRequest;
import app.dto.NoteResponse;
import app.dto.NoteUpdateRequest;
import app.model.Note;
import app.repository.NoteRepository;
import app.repository.TicketRepository;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuthService authService;

    public List<NoteResponse> getNotes(Long ticketId) {
        return noteRepository.findByTicketId(ticketId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NoteResponse createNote(Long userId, NoteCreateRequest req) {

        // Validate ticket exists
        ticketRepository.findById(req.ticketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Note note = new Note();
        note.setTicketId(req.ticketId());
        note.setBody(req.body());
        note.setCreatedBy(userId);
        note.setUpdatedBy(userId);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());

        return toResponse(noteRepository.save(note));
    }

    public NoteResponse updateNote(Long userId, Long noteId, NoteUpdateRequest req) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        boolean hasManagePermission = authService.hasPermission("note.manage");

        // SUPPORT can only edit their own notes
        if (!hasManagePermission && !note.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("You can only update notes you created");
        }

        note.setBody(req.body());
        note.setUpdatedBy(userId);
        note.setUpdatedAt(LocalDateTime.now());

        return toResponse(noteRepository.save(note));
    }

    public void deleteNote(Long userId, Long noteId) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        boolean hasManagePermission = authService.hasPermission("note.manage");

        // SUPPORT can only delete their own notes
        if (!hasManagePermission && !note.getCreatedBy().equals(userId)) {
            throw new AccessDeniedException("You can only delete notes you created");
        }

        noteRepository.delete(note);
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTicketId(),
                note.getCreatedBy(),
                note.getUpdatedBy(),
                note.getBody(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
