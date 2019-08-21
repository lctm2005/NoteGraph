package com.licong.notemap.service.internal;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.SyncState;
import com.licong.notemap.domain.Note;
import com.licong.notemap.domain.Link;
import com.licong.notemap.repository.EvernoteRepository;
import com.licong.notemap.repository.NoteRepository;
import com.licong.notemap.repository.LinkRepository;
import com.licong.notemap.service.EvernoteSyncService;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.XmlUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lctm2005 on 2017/4/18.
 */
@Slf4j
@Service
public class EvernoteSyncServiceImpl implements EvernoteSyncService {

    private static int LATEST_UPDATE_COUNT = 0;
    private static int LIMIT = 50;
    private static final String INNER_LINK = "https://app.yinxiang.com";
    private static final String INNER_LINK_2 = "evernote:///";

    @Autowired
    private EvernoteRepository evernoteRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public void syncNote(Long userId, UUID noteId) {
        // TODO userId -> auth
        com.evernote.edam.type.Note note = evernoteRepository.get(noteId);
    }


    private List<com.evernote.edam.type.Note> findNotes(NoteFilter noteFilter) {
        //接口限制最多返回50个
        List<com.evernote.edam.type.Note> notes = new ArrayList<>(50);
        int offset = 0;
        int total;
        do {
            NoteList noteList = evernoteRepository.findNotes(noteFilter, offset, LIMIT);
            total = noteList.getTotalNotes();
            notes.addAll(noteList.getNotes());
            offset += LIMIT;
        } while (total - offset > 0);
        return notes;
    }


    @Override
    public void syncNoteBook(Long userId, UUID noteBookId) {
        NoteFilter noteFilter = new NoteFilter();
        noteFilter.setNotebookGuid(noteBookId.toString());
        List<com.evernote.edam.type.Note> noteList = findNotes(noteFilter);
        List<Note> notes = new ArrayList<>();
        List<TempLink> tempLinks = new ArrayList<>();

        for (com.evernote.edam.type.Note note : noteList) {
            try {
                // add knowledge to notes
                Note knowledge = new Note();
                knowledge.setUuid(UUID.fromString(note.getGuid()));
                knowledge.setTitle(note.getTitle());
                notes.add(knowledge);

                String content = evernoteRepository.getNoteContent(UUID.fromString(note.getGuid()));
                List<XmlUtils.Href> hrefs = XmlUtils.extractHrefs(content);

                // add link to tempLinks
                for (XmlUtils.Href href : hrefs) {
                    String hrefUrl = href.getUrl();
                    String hrefName = href.getName();
                    if (hrefUrl.contains(INNER_LINK)) {
                        TempLink tempLink = new TempLink();
                        tempLink.setTitle(hrefName);
                        tempLink.setStart(knowledge.getUuid());
                        tempLink.setEnd(UUID.fromString(hrefUrl.substring(hrefUrl.lastIndexOf('/') + 1)));
                        tempLinks.add(tempLink);
                    }
                    if (hrefUrl.contains(INNER_LINK_2)) {
                        TempLink tempLink = new TempLink();
                        tempLink.setTitle(hrefName);
                        tempLink.setStart(knowledge.getUuid());
                        tempLink.setEnd(UUID.fromString(hrefUrl.substring(hrefUrl.length() - 37, hrefUrl.length() - 1)));
                        tempLinks.add(tempLink);
                    }
                }
            } catch (Exception e) {
                log.error("parse note failed:", e);
            }
        }

        // add link to list
        Map<UUID, Note> knowledgeMap = CollectionUtils.getPropertyMap(notes, "uuid");
        List<Link> links = new ArrayList<>();
        for (TempLink tempLink : tempLinks) {
            Link link = new Link();
            link.setTitle(tempLink.getTitle());
            link.setStart(knowledgeMap.get(tempLink.getStart()));
            link.setEnd(knowledgeMap.get(tempLink.getEnd()));
            if (link.getEnd() != null) {
                links.add(link);
            }
        }

        noteRepository.deleteAll();
        linkRepository.deleteAll();
        noteRepository.saveAll(notes);
        linkRepository.saveAll(links);
    }

    @Override
    public void sync() {
        // Each time you want to check for new and updated notes...
        SyncState currentState = evernoteRepository.getSyncState();
        int currentUpdateCount = currentState.getUpdateCount();

        if (currentUpdateCount > LATEST_UPDATE_COUNT) {
            // Keep track of the new high-water mark
            LATEST_UPDATE_COUNT = currentUpdateCount;
        }
    }

    @Data
    class TempLink {
        private UUID start;
        private UUID end;
        private String title;
    }
}
