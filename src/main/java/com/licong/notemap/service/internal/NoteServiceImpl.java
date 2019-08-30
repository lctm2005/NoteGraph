package com.licong.notemap.service.internal;

import com.licong.notemap.repository.neo4j.Link;
import com.licong.notemap.repository.neo4j.LinkRepository;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.repository.mongo.NoteContent;
import com.licong.notemap.repository.neo4j.Node;
import com.licong.notemap.repository.mongo.NoteContentRepository;
import com.licong.notemap.repository.neo4j.NodeRepository;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.NoteInnerLinkUtils;
import com.licong.notemap.util.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private NoteContentRepository noteContentRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Note> findAll() {
        Iterable<Node> nodes = nodeRepository.findAll();
        List<Note> notes = new ArrayList<>();
        for (Node node : nodes) {
            notes.add(new Note(node));
        }
        return notes;
    }

    @Override
    public Note findById(UUID noteId) {
        Optional<Node> nodeOptional = nodeRepository.findByUuid(noteId);
        if (!nodeOptional.isPresent()) {
            return null;
        }
        Optional<NoteContent> noteContentOptional = noteContentRepository.findById(noteId);
        if (noteContentOptional.isPresent()) {
            return new Note(nodeOptional.get(), noteContentOptional.get());
        } else {
            return new Note(nodeOptional.get());
        }
    }

    @Override
    public Note save(Note note) {
        Optional<Node> nodeOptional;
        if (null == note.getId()) {
            note.generateId();
            nodeOptional = Optional.ofNullable(null);
        } else {
            nodeOptional = nodeRepository.findByUuid(note.getId());
        }
        Node node;
        if (nodeOptional.isPresent()) {
            node = nodeOptional.get();
        } else {
            node = new Node();
            node.setUuid(note.getId());
        }
        node.setTitle(note.getTitle());
        nodeRepository.save(node);

        Optional<NoteContent> noteContentOptional = noteContentRepository.findById(note.getId());
        NoteContent noteContent;
        if (noteContentOptional.isPresent()) {
            noteContent = noteContentOptional.get();
        } else {
            noteContent = new NoteContent();
            noteContent.setUuid(note.getId());
        }
        noteContent.setMarkdown(note.getContent());
        noteContentRepository.save(noteContent);

        List<NoteInnerLinkUtils.NoteInnerLink> noteInnerLinks = NoteInnerLinkUtils.parse(note.getContent());
        if (CollectionUtils.isNotEmpty(noteInnerLinks)) {
            List<UUID> noteIds = CollectionUtils.getPropertyList(noteInnerLinks, "noteId");
            List<Node> nodes = nodeRepository.findByUuidIn(noteIds);
            Map<UUID, Node> nodeMap = CollectionUtils.getPropertyMap(nodes, "uuid");

            List<Link> links = new ArrayList<>();
            for (NoteInnerLinkUtils.NoteInnerLink noteInnerLink : noteInnerLinks) {
                Link link = new Link();
                link.setStart(node);
                link.setEnd(nodeMap.get(noteInnerLink.getNoteId()));
                link.setTitle(noteInnerLink.getTitle());
                links.add(link);
            }
            linkRepository.saveAll(links);
        }

        return note;
    }
}
