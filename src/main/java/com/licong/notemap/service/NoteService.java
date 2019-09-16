package com.licong.notemap.service;

import com.licong.notemap.service.domain.Note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {

    List<Note> findAll();

    Optional<Note> findById(UUID noteId);

    Note save(Note note);

    Optional<Note> delete(UUID noteId);

}
