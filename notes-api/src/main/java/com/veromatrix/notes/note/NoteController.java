package com.veromatrix.notes.note;

import com.veromatrix.notes.note.dto.NoteRequest;
import com.veromatrix.notes.note.dto.NoteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
@CrossOrigin
public class NoteController {
    private final NoteService service;

    @GetMapping(path = "/all")
    public List<NoteResponse> all(@RequestParam(value = "period",required = false)String period) throws NotesApiUsageException {
        if(StringUtils.hasText(period)){
            return getByPeriod(period);
        }
        return service.getAll();
    }

    public List<NoteResponse> getByPeriod(String period) throws NotesApiUsageException { return service.getResultsForPeriod(period); }

    @GetMapping(path = "/today")
    public List<NoteResponse> getTodaysNotes() throws NotesApiUsageException { return service.getDailyNotes(); }

    @GetMapping(path = "/{id}")
    public NoteResponse byId(@PathVariable String id) { return service.getById(id); }

    @PostMapping
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest req){
        NoteResponse noteResponse = service.create(req);
        return ResponseEntity.created(URI.create("/api/v1/notes/" + noteResponse.getId()))
                .body(noteResponse);
    }

    @PutMapping("/{id}")
    public NoteResponse update(@PathVariable String id, @Valid @RequestBody NoteRequest req){
        return service.update(id, req);
    }

    @PatchMapping("/{id}")
    public NoteResponse patch(@PathVariable String id, @RequestBody NoteRequest req){
        return service.patch(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){ service.delete(id); }
}
