package com.licong.notemap.service;

import com.licong.notemap.service.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface NoteService {

    Optional<Note> findById(UUID noteId);

    Note save(Note note);

    void delete(UUID noteId);

    Page<Note> findByTitleContains(String title, String tag, Pageable pageable);

    List<Note> neighbours(UUID noteId);
}
