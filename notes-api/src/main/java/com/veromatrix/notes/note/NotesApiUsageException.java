package com.veromatrix.notes.note;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotesApiUsageException extends RuntimeException {
    public NotesApiUsageException(Throwable e) {
        super(e);
    }
}
