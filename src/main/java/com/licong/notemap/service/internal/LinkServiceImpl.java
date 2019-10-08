package com.licong.notemap.service.internal;

import com.licong.notemap.repository.mongo.NoteContent;
import com.licong.notemap.repository.neo4j.Link;
import com.licong.notemap.repository.neo4j.LinkRepository;
import com.licong.notemap.repository.neo4j.Node;
import com.licong.notemap.repository.neo4j.NodeRepository;
import com.licong.notemap.service.LinkService;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.service.domain.NoteLink;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private NoteService noteService;

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<NoteLink> findAll() {
        List<Link> links = new ArrayList<>();
        CollectionUtils.addAll(links, linkRepository.findAll().iterator());
        return links.stream().map(e -> NoteLink.convert(e)).collect(Collectors.toList());
    }

    @Override
    public List<NoteLink> findByNotes(List<UUID> noteIds) {
        List<Link> links = linkRepository.findByStartInOrEndIn(noteIds.stream().map(e -> e.toString()).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(links)) {
            return Collections.emptyList();
        }
        return links.stream().map(e -> NoteLink.convert(e)).collect(Collectors.toList());
    }

    @Override
    public void rebuild() {
        linkRepository.deleteAll();
        noteService.findAll().stream().forEach(note -> noteService.save(note));
    }
}
