package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.NoteRepository;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.NoteInnerLinkUtils;
import com.licong.notemap.util.StringUtils;
import com.licong.notemap.web.vo.note.NoteResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Optional<Note> findById(UUID noteId) {
        return noteRepository.findById(noteId);
    }

    @Override
    public Note save(Note note) {
        note.setReference(extractLinks(note));
        return noteRepository.save(note);
    }


    /**
     * 提取引用的笔记
     *
     * @param note
     * @return
     */
    private List<Note> extractLinks(Note note) {
        List<NoteInnerLinkUtils.NoteInnerLink> noteInnerLinks = NoteInnerLinkUtils.parse(note.getContent());
        if (CollectionUtils.isEmpty(noteInnerLinks)) {
            return Collections.emptyList();
        }
        List<UUID> noteIds = noteInnerLinks.stream().map(noteInnerLink -> noteInnerLink.getNoteId()).collect(Collectors.toList());
        return noteRepository.findByIdIn(noteIds);
    }


    @Override
    public void delete(UUID noteId) {
        noteRepository.deleteById(noteId);
    }

    @Override
    public Page<Note> findByTitleContains(String title, Pageable pageable) {
        if (StringUtils.isEmpty(title)) {
            return noteRepository.findAll(pageable);
        } else {
            Page<Note> notes = noteRepository.findByTitleLike("(?i).*" + title + ".*", pageable);
            List<UUID> noteIds = CollectionUtils.getPropertyList(notes.getContent(),"id");
            List<Note> results = noteRepository.findByIdIn(noteIds);
            Map<UUID, Note> resultMap = CollectionUtils.getPropertyMap(results, "id");
            return notes.map(e -> resultMap.get(e.getId()));
        }
    }

    @Override
    public List<Note> neighbours(UUID noteId) {
        List<Note> notes = noteRepository.neighbours(noteId);
        List<UUID> noteIds = CollectionUtils.getPropertyList(notes,"id");
        return noteRepository.findByIdIn(noteIds);
    }


}
