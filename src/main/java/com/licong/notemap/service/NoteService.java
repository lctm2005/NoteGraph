package com.licong.notemap.service;

import com.licong.notemap.domain.Note;

public interface NoteService {

    Iterable<Note> findAll();
}
