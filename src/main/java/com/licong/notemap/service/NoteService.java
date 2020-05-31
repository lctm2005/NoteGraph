package com.licong.notemap.service;

import com.licong.notemap.service.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {

    List<Note> findAll();

    Optional<Note> findById(UUID noteId);

    Note save(Note note) ;

    Optional<Note> delete(UUID noteId);

    Page<Note> findByTitleContains(String title, Pageable pageable);

    List<Note> neighbours(UUID noteId);
}
