package com.veromatrix.notes.note;

import com.veromatrix.notes.note.dto.NoteRequest;
import com.veromatrix.notes.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository repo;

    public List<NoteResponse> getAll() {
        return repo.findAll().stream().map(this::toResp).toList();
    }
    public List<NoteResponse> getResultsForPeriod(String period) throws NotesApiUsageException {
        PeriodRange periodRange = rangeFromPeriod(period);
        return repo.findByCreatedTsBetweenOrderByCreatedTsDesc(periodRange.from(),periodRange.to())
                .stream()
                .map(this::toResp).toList();
    }

    public NoteResponse create(NoteRequest r) {
        Note n = Note.builder()
                .title(r.getTitle().trim())
                .content(r.getContent().trim())
                .category(r.getCategory())
                .subCategory(r.getSubCategory())
                .pinned(Boolean.TRUE.equals(r.getPinned()))
                .hoursSpent(r.getHoursSpent())
                .noteDay(r.getNoteDay())
                .noteTime(r.getNoteTime())
                .createdTs(LocalDateTime.now())
                .createdBy(r.getUser())
                .build();
        return toResp(repo.save(n));
    }

    public NoteResponse update(String id, NoteRequest r) {
        Note n = repo.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        n.setTitle(r.getTitle().trim());
        n.setContent(r.getContent().trim());
        n.setCategory(r.getCategory());
        n.setSubCategory(r.getSubCategory());
        n.setPinned(Boolean.TRUE.equals(r.getPinned()));
        n.setHoursSpent(r.getHoursSpent());
        n.setNoteDay(r.getNoteDay());
        n.setNoteTime(r.getNoteTime());
        n.setUpdatedTs(LocalDateTime.now());
        n.setUpdatedBy(r.getUser());
        return toResp(repo.save(n));
    }

    public NoteResponse patch(String id, NoteRequest r) {
        Note n = repo.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        // Patch only if field is present (non-null)
        if (r.getTitle() != null && !r.getTitle().isBlank()) {
            n.setTitle(r.getTitle().trim());
        }

        if (r.getContent() != null && !r.getContent().isBlank()) {
            n.setContent(r.getContent().trim());
        }

        if (r.getCategory() != null) {
            n.setCategory(r.getCategory());
        }

        if (r.getSubCategory() != null) {
            n.setSubCategory(r.getSubCategory());
        }

        if (r.getPinned() != null) {
            n.setPinned(r.getPinned());
        }

        if (r.getHoursSpent() != null) {
            n.setHoursSpent(r.getHoursSpent());
        }

        if (r.getNoteDay() != null) {
            n.setNoteDay(r.getNoteDay());
        }

        if (r.getNoteTime() != null) {
            n.setNoteTime(r.getNoteTime());
        }

        // PATCH-specific fields
        n.setUpdatedTs(LocalDateTime.now());

        if (r.getUser() != null) {
            n.setUpdatedBy(r.getUser());
        }

        return toResp(repo.save(n));
    }

    public void delete(String id) { repo.deleteById(id); }

    private Instant parse(String iso, Instant def){
        if(!StringUtils.hasText(iso)) return def;
        try { return Instant.parse(iso); } catch(Exception e){ return def; }
    }

    private NoteResponse toResp(Note n){
        return NoteResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .content(n.getContent())
                .category(n.getCategory())
                .subCategory(n.getSubCategory())
                .pinned(String.valueOf(n.isPinned()))
                .noOfHrs(n.getHoursSpent() == null?"":String.valueOf(n.getHoursSpent()))
                .noteTime(String.valueOf(n.getNoteTime()))
                .noteDay(String.valueOf(n.getNoteDay()))
                .createdBy(n.getCreatedBy())
                .createdTS(String.valueOf(n.getCreatedTs()))
                .updatedBy(n.getUpdatedBy() == null?"":n.getUpdatedBy())
                .updatedTS(n.getUpdatedTs() == null?"":String.valueOf(n.getUpdatedTs()))
                .build();
    }

    public NoteResponse getById(String id) {
        return toResp(repo.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id)));
    }

    public List<NoteResponse> getDailyNotes() throws NotesApiUsageException {
        PeriodRange today = rangeFromPeriod("1d");
        List<Note> notes = repo.findByCreatedTsBetweenOrderByCreatedTsDesc(today.from(), today.to());
        return notes.stream().map(this::toResp).toList();
    }

    private PeriodRange rangeFromPeriod(String period) throws NotesApiUsageException {
        Instant now = Instant.now();

        Duration duration = parseDuration(period);
        Instant from = now.minus(duration);

        return new PeriodRange(from, now);
    }

    private Duration parseDuration(String period) throws NotesApiUsageException {
        if (period == null || period.isBlank()) {
            return Duration.ofDays(1); // default
        }

        String p = period.trim().toLowerCase();

        try {
            long l = Long.parseLong(p.substring(0, p.length() - 1));
            if (p.endsWith("d")) {
                return Duration.ofDays(l);
            }
            if (p.endsWith("h")) {
                return Duration.ofHours(l);
            }
        } catch (NumberFormatException e) {
            throw new NotesApiUsageException(new IllegalArgumentException("Invalid period: " + period));
        }

        throw new IllegalArgumentException("Invalid period. Use like 7d, 24h");
    }

    private record PeriodRange(Instant from, Instant to) {}
}