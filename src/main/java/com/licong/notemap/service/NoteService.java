package com.licong.notemap.service;

import com.licong.notemap.service.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface NoteService {

    Optional<Note> findById(Long noteId);

    Note save(Note note);

    void delete(Long noteId);

    Page<Note> findByTitleContains(String title, Pageable pageable);

    List<Note> neighbours(Long noteId);
}
