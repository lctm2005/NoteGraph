package com.licong.notemap.service.internal;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.licong.notemap.domain.Link;
import com.licong.notemap.repository.EvernoteRepository;
import com.licong.notemap.repository.LinkRepository;
import com.licong.notemap.repository.NoteRepository;
import com.licong.notemap.service.EvernoteSyncService;
import com.licong.notemap.util.Href;
import com.licong.notemap.util.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/18.
 */
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
        Note note = evernoteRepository.get(noteId);
    }


    private List<Note> findNotes(NoteFilter noteFilter) {
        //接口限制最多返回50个
        List<Note> notes = new ArrayList<>(50);
        int offset = 0;
        int total;
        do {
            NoteList noteList = evernoteRepository.findNotes(noteFilter, offset, LIMIT);
            total = noteList.getTotalNotes();
            notes.addAll(noteList.getNotes());
            offset+=LIMIT;
        } while (total - offset > 0);
        return notes;
    }


    @Override
    public void syncNoteBook(Long userId, UUID noteBookId) {
        NoteFilter noteFilter = new NoteFilter();
        noteFilter.setNotebookGuid(noteBookId.toString());
        List<Note> noteList = findNotes(noteFilter);
        List<com.licong.notemap.domain.Note> notes = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        for (Note note : noteList) {
            com.licong.notemap.domain.Note noteDomain = new com.licong.notemap.domain.Note();
            noteDomain.setUuid(UUID.fromString(note.getGuid()));
            noteDomain.setName(note.getTitle());
            notes.add(noteDomain);
            String content = evernoteRepository.getNoteContent(UUID.fromString(note.getGuid()));
            List<Href> hrefs = XmlUtils.extractHrefs(content);
            for (Href href : hrefs) {
                String url = href.getUrl();
                String value = href.getName();
                if (url.contains(INNER_LINK)) {
                    Link link = new Link();
                    link.setName(value);
                    link.setSource(noteDomain.getUuid());
                    link.setTarget(UUID.fromString(url.substring(url.lastIndexOf('/') + 1)));
                    links.add(link);
                }
                if (url.contains(INNER_LINK_2)) {
                    Link link = new Link();
                    link.setName(value);
                    link.setSource(noteDomain.getUuid());
                    link.setTarget(UUID.fromString(url.substring(url.length() - 37, url.length() - 1)));
                    links.add(link);
                }
            }
        }
        noteRepository.deleteAll();
        linkRepository.deleteAll();
        noteRepository.save(notes);
        linkRepository.save(links);
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
}
