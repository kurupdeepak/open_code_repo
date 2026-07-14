package com.veromatrix.notes.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class NoteRequest {
    @NotBlank @Size(max=120)
    private String title;
    @NotBlank
    private String content;

    private Boolean pinned;

    private String category;

    @JsonProperty("sub_category")
    private String subCategory;

    @JsonProperty("hours_spent")
    private Double hoursSpent;

    @JsonProperty("note_day")
    private LocalDate noteDay;

    @JsonProperty("note_time")
    private LocalTime noteTime;

    @JsonProperty("user")
    private String user;
}
