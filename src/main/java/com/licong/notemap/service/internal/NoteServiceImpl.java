package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.NoteRepository;
import com.licong.notemap.repository.neo4j.TagRepository;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.service.domain.Tag;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.NoteInnerLinkUtils;
import com.licong.notemap.util.NoteTagUtils;

import static com.licong.notemap.util.StringUtils.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Optional<Note> findById(UUID noteId) {
        return noteRepository.findById(noteId);
    }

    @Override
    public Note save(Note note) {
        note.setReference(extractLinks(note));
        note.setTag(extractTags(note));
        return noteRepository.save(note);
    }

    private List<Tag> extractTags(Note note) {
        List<String> titles = NoteTagUtils.parseTags(note.getContent());
        if (CollectionUtils.isEmpty(titles)) return Collections.emptyList();
        List<Tag> tags = tagRepository.findByTitleIn(titles);
        Map<String, Tag> tagMap = CollectionUtils.getPropertyMap(tags, "title");
        List<Tag> newTags = new ArrayList<>();
        for (String tag : titles) {
            if (null == tagMap.get(tag)) {
                Tag newTag = new Tag();
                newTag.setTitle(tag);
                newTags.add(newTag);
            }
        }
        newTags = tagRepository.saveAll(newTags);
        tags.addAll(newTags);
        return tags;
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
    public Page<Note> findByTitleContains(String title, String tag, Pageable pageable) {
        if (isEmpty(title) && isEmpty(tag)) {
            return noteRepository.findAll(pageable);
        }
        if (isNotEmpty(title) && isEmpty(tag)) {
            Page<Note> notes = noteRepository.findByTitleLike("(?i).*" + title + ".*", pageable);
            return research(notes);
        }
        if (isNotEmpty(title) && isNotEmpty(tag)) {
            Page<Note> notes = noteRepository.findByTitleLikeAndTagEquals("(?i).*" + title + ".*", tag, pageable);
            return research(notes);
        }
        return noteRepository.findByTagEquals(tag, pageable);
    }

    /**
     * 重新查询确保关联对象可以查出来
     * @param notes
     * @return
     */
    private Page<Note> research(Page<Note> notes) {
        List<UUID> noteIds = CollectionUtils.getPropertyList(notes.getContent(), "id");
        List<Note> results = noteRepository.findByIdIn(noteIds);
        Map<UUID, Note> resultMap = CollectionUtils.getPropertyMap(results, "id");
        return notes.map(e -> resultMap.get(e.getId()));
    }

    @Override
    public List<Note> neighbours(UUID noteId) {
        List<Note> notes = noteRepository.neighbours(noteId);
        List<UUID> noteIds = CollectionUtils.getPropertyList(notes, "id");
        return noteRepository.findByIdIn(noteIds);
    }


}
