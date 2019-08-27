package com.licong.notemap.service.internal;

import com.licong.notemap.domain.Note;
import com.licong.notemap.repository.neo4j.NoteRepository;
import com.licong.notemap.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Iterable<Note> findAll() {
        return noteRepository.findAll();
    }
}
