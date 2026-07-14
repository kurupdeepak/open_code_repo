package com.veromatrix.notes.note;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByCreatedTsBetweenOrderByCreatedTsDesc(
            Instant from, Instant to
    );
}
