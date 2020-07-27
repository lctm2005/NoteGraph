package com.licong.notemap.service.internal;

import com.licong.notemap.repository.mongo.NoteContent;
import com.licong.notemap.repository.mongo.NoteContentRepository;
import com.licong.notemap.repository.neo4j.Link;
import com.licong.notemap.repository.neo4j.LinkRepository;
import com.licong.notemap.repository.neo4j.Node;
import com.licong.notemap.repository.neo4j.NodeRepository;
import com.licong.notemap.service.EverNoteService;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.NoteInnerLinkUtils;
import com.licong.notemap.util.StringUtils;
import com.licong.notemap.web.security.EvernoteAccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private NoteContentRepository noteContentRepository;

    @Autowired
    private EverNoteService everNoteService;


    @Override
    public List<Note> findAll() {
        Iterable<Node> nodes = nodeRepository.findAll();
        List<NoteContent> noteContents = noteContentRepository.findAll();
        Map<UUID, NoteContent> noteContentMap = com.licong.notemap.util.CollectionUtils.getPropertyMap(noteContents, "uuid");
        List<Note> notes = new ArrayList<>();
        for (Node node : nodes) {
            notes.add(new Note(node, noteContentMap.get(UUID.fromString(node.getUniqueId()))));
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
        //更新Note节点
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

        // 保存印象笔记
        EvernoteAccessToken accessToken = (EvernoteAccessToken) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        com.evernote.edam.type.Note everNote = everNoteService.save(node.getEverNoteId(), note, accessToken);

        // 更新Note节点中的印象笔记ID
        node.setEverNoteId(everNote.getGuid());
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
        oldlinks = linkRepository.findByStartAndEndAndTitle(oldlinks);
        linkRepository.deleteAll(oldlinks);

        // 更新内容
        noteContent.setMarkdown(note.getMarkdown());
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

    @Override
    public Page<Note> findByTitleContains(String title, Pageable pageable) {
        Page<Node> page;
        if (StringUtils.isEmpty(title)) {
            page = nodeRepository.findAll(pageable);
        } else {
            page = nodeRepository.findByTitleLike("(?i).*" + title + ".*", pageable);
        }
        if (page.isEmpty()) {
            return Page.empty();
        }
        return page.map(e -> new Note(e));
    }

    @Override
    public List<Note> neighbours(UUID noteId) {
        List<Node> nodes = nodeRepository.neighbours(noteId);
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }
        List<Note> notes = new ArrayList<>();
        for (Node node : nodes) {
            notes.add(new Note(node));
        }
        return notes;
    }


}
