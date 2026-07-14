package com.veromatrix.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veromatrix.notes.note.NoteController;
import com.veromatrix.notes.note.NoteService;
import com.veromatrix.notes.note.dto.NoteRequest;
import com.veromatrix.notes.note.dto.NoteResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean
    NoteService service;

    @Test
    void all_withPeriod_callsServicePeriod() throws Exception {
        NoteResponse noteResponse = NoteResponse.builder().id("1")
                .title("t").build();
        when(service.getResultsForPeriod("7d"))
                .thenReturn(List.of(noteResponse));

        mvc.perform(get("/api/v1/notes/all").param("period", "7d"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("t"));

        verify(service).getResultsForPeriod("7d");
        verify(service, never()).getAll();
    }

    @Test
    void all_withBlankPeriod_callsGetAll() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        // note: your controller requires period param, so we pass it blank
        mvc.perform(get("/api/v1/notes/all").param("period", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(service).getAll();
        verify(service, never()).getResultsForPeriod(anyString());
    }

    @Test
    void today_returnsDailyNotes() throws Exception {
        when(service.getDailyNotes()).thenReturn(List.of(NoteResponse.builder().id("9")
                .pinned("true").build()));

        mvc.perform(get("/api/v1/notes/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("9"))
                .andExpect(jsonPath("$[0].pinned").value("true"));

        verify(service).getDailyNotes();
    }

    @Test
    void byId_returnsNote() throws Exception {
        when(service.getById("abc"))
                .thenReturn(NoteResponse.builder().id("abc").title("T").build());

        mvc.perform(get("/api/v1/notes/{id}", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc"))
                .andExpect(jsonPath("$.title").value("T"));

        verify(service).getById("abc");
    }

    @Test
    void create_returns201_andLocationHeader() throws Exception {
        NoteRequest req = new NoteRequest();
        req.setTitle("Hello");
        req.setContent("Body");
        req.setCategory("Work");
        req.setSubCategory("Dev");
        req.setPinned(false);
        req.setHoursSpent(1.25);

        when(service.create(ArgumentMatchers.any(NoteRequest.class)))
                .thenReturn(NoteResponse.builder().id("n1").title("Hello").build());

        mvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/notes/n1"))
                .andExpect(jsonPath("$.id").value("n1"))
                .andExpect(jsonPath("$.title").value("Hello"));

        verify(service).create(any(NoteRequest.class));
    }

    @Test
    void create_invalid_returns400() throws Exception {
        // title blank violates @NotBlank; content blank violates @NotBlank
        NoteRequest req = new NoteRequest();
        req.setTitle(" ");
        req.setContent(" ");

        mvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    void update_returns200() throws Exception {
        NoteRequest req = new NoteRequest();
        req.setTitle("Upd");
        req.setContent("Body");

        when(service.update(eq("id1"), any(NoteRequest.class)))
                .thenReturn(NoteResponse.builder().id("id1").title("Upd").build());

        mvc.perform(put("/api/v1/notes/{id}", "id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("Upd"));

        verify(service).update(eq("id1"), any(NoteRequest.class));
    }

    @Test
    void patch_returns200() throws Exception {
        NoteRequest req = new NoteRequest();
        req.setTitle("Patch");
        // patch allows partial, so no @Valid on patch in your controller

        when(service.patch(eq("id2"), any(NoteRequest.class)))
                .thenReturn(NoteResponse.builder().id("id2").title("Patch").build());

        mvc.perform(patch("/api/v1/notes/{id}", "id2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id2"))
                .andExpect(jsonPath("$.title").value("Patch"));

        verify(service).patch(eq("id2"), any(NoteRequest.class));
    }

    @Test
    void delete_returns200() throws Exception {
        doNothing().when(service).delete("id3");

        mvc.perform(delete("/api/v1/notes/{id}", "id3"))
                .andExpect(status().isOk());

        verify(service).delete("id3");
    }
}