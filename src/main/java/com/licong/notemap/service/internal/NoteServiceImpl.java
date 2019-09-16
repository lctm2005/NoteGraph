package com.licong.notemap.service.internal;

import com.licong.notemap.repository.mongo.NoteContent;
import com.licong.notemap.repository.mongo.NoteContentRepository;
import com.licong.notemap.repository.neo4j.*;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.NoteInnerLinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<Note> findById(UUID noteId) {
        Optional<Node> nodeOptional = nodeRepository.findByUniqueId(noteId);
        if (!nodeOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        Optional<NoteContent> noteContentOptional = noteContentRepository.findById(noteId);
        if (noteContentOptional.isPresent()) {
            return Optional.of(new Note(nodeOptional.get(), noteContentOptional.get()));
        } else {
            return Optional.of(new Note(nodeOptional.get()));
        }
    }

    @Override
    public Note save(Note note) {
        //更新标题
        Optional<Node> nodeOptional;
        if (null == note.getId()) {
            note.generateId();
            nodeOptional = Optional.ofNullable(null);
        } else {
            nodeOptional = nodeRepository.findByUniqueId(note.getId());
        }
        Node node;
        if (nodeOptional.isPresent()) {
            node = nodeOptional.get();
        } else {
            node = new Node();
            node.setUniqueId(note.getId().toString());
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

        // 删除旧关系
        List<Link> oldlinks = extractLinks(node, noteContent);
        linkRepository.deleteAll(oldlinks);

        // 更新内容
        noteContent.setMarkdown(note.getContent());
        noteContentRepository.save(noteContent);

        // 保存新关系
        List<Link> links = extractLinks(node, noteContent);
        linkRepository.mergeAll(links);
        return note;
    }


    /**
     * 提取连接
     *
     * @param node
     * @param noteContent
     * @return
     */
    private List<Link> extractLinks(Node node, NoteContent noteContent) {
        List<NoteInnerLinkUtils.NoteInnerLink> noteInnerLinks = NoteInnerLinkUtils.parse(noteContent.getMarkdown());
        if (CollectionUtils.isEmpty(noteInnerLinks)) {
            return Collections.emptyList();
        }
        List<String> noteIds = noteInnerLinks.stream().map(noteInnerLink -> noteInnerLink.getNoteId().toString()).collect(Collectors.toList());
        List<Node> nodes = nodeRepository.findByUniqueIdIn(noteIds);

        Map<UUID, Node> nodeMap = nodes.stream().collect(Collectors.toMap(n -> UUID.fromString(n.getUniqueId()), (n) -> n));

        List<Link> links = new ArrayList<>();
        for (NoteInnerLinkUtils.NoteInnerLink noteInnerLink : noteInnerLinks) {
            Link link = new Link();
            link.setStart(node);
            link.setEnd(nodeMap.get(noteInnerLink.getNoteId()));
            link.setTitle(noteInnerLink.getTitle());
            links.add(link);
        }
        return links;
    }

    @Override
    public Optional<Note> delete(UUID noteId) {
        Optional<Node> nodeOptional = nodeRepository.findByUniqueId(noteId);
        if (!nodeOptional.isPresent()) {
            return Optional.ofNullable(null);
        }
        Optional<NoteContent> noteContentOptional = noteContentRepository.findById(noteId);
        if (noteContentOptional.isPresent()) {
            NoteContent noteContent = noteContentOptional.get();
            List<Link> links = extractLinks(nodeOptional.get(), noteContent);
            // 删关系
            linkRepository.deleteAll(links);
            noteContentRepository.delete(noteContent);
        }
        nodeRepository.delete(nodeOptional.get());
        return Optional.of(new Note(nodeOptional.get()));
    }
}
