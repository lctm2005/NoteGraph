package com.licong.notemap.repository.internal;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;
import com.licong.notemap.repository.EvernoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/20.
 */
@Slf4j
@Repository
public class EvernoteRepositoryImpl implements EvernoteRepository {
    private static final String AUTH_TOKEN = "S=s31:U=5a2e79:E=162cca47472:C=15b74f34640:P=1cd:A=en-devtoken:V=2:H=a6618a6fdb6611800841e3e17179c18d";
    private static final String NOTE_STORE_URL = "https://app.yinxiang.com/shard/s31/notestore";
    private static final String USER_AGENT = "JamesLee/EverNoteMap/1.0.0";

    private NoteStore.Client noteStore;

    public EvernoteRepositoryImpl() throws TTransportException {
        THttpClient noteStoreTrans = new THttpClient(NOTE_STORE_URL);
        noteStoreTrans.setCustomHeader("User-Agent", USER_AGENT);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        noteStore = new NoteStore.Client(noteStoreProt, noteStoreProt);
    }

    public Note get(UUID noteId) {
        try {
            return noteStore.getNote(AUTH_TOKEN, noteId.toString(), true, true, true, true);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public SyncState getSyncState() {
        try {
            return noteStore.getSyncState(AUTH_TOKEN);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public NoteList findNotes(NoteFilter noteFilter, Integer offset, Integer limit) {
        try {
            return noteStore.findNotes(AUTH_TOKEN, noteFilter, offset, limit);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public String getNoteContent(UUID noteId) {
        try {
            return noteStore.getNoteContent(AUTH_TOKEN, noteId.toString());
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
