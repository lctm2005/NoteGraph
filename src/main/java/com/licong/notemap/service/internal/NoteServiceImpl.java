package com.licong.notemap.service.internal;

import com.licong.notemap.domain.Note;
import com.licong.notemap.repository.NoteRepository;
import com.licong.notemap.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lctm2005 on 2017/4/18.
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<Note> findAll() {
        Iterable<Note> notes =  noteRepository.findAll();
        Iterator<Note> iterator = notes.iterator();
        List<Note> results = new ArrayList<>();
        while(iterator.hasNext()) {
            Note note = iterator.next();
            results.add(note);
        }
        return results;
    }
}
