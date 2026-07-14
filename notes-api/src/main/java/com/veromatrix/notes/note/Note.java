package com.veromatrix.notes.note;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    private String title;

    private String content;

    private String category;

    @Field("sub_category")
    private String subCategory;

    private boolean pinned;

    @Field("hours_spent")
    private Double hoursSpent;

    @Field("note_day")
    private LocalDate noteDay;

    @Field("note_hr")
    private LocalTime noteTime;

    @Field("created_ts")
    private LocalDateTime createdTs;

    @Field("created_by")
    private String createdBy;

    @Field("updated_ts")
    private LocalDateTime updatedTs;

    @Field("updated_by")
    private String updatedBy;
}
