package com.veromatrix.notes.note.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
@Builder
@JsonInclude(NON_EMPTY)
public class NoteResponse {
    private String id;
    private String title;
    private String content;
    private String category;

    @JsonProperty("sub_category")
    private String subCategory;

    private String pinned;

    @JsonProperty("hours_spent")
    private String noOfHrs;

    @JsonProperty("note_day")
    private String noteDay;

    @JsonProperty("note_time")
    private String noteTime;

    @JsonProperty("created_ts")
    private String createdTS;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_ts")
    @JsonInclude(NON_EMPTY)
    private String updatedTS;

    @JsonInclude(NON_EMPTY)
    @JsonProperty("updated_by")
    private String updatedBy;
}
