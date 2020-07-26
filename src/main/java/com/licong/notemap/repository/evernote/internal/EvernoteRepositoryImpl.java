package com.licong.notemap.repository.evernote.internal;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.User;
import com.evernote.edam.userstore.UserStore;
import com.evernote.thrift.TException;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;
import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/20.
 */
@Slf4j
@Repository
public class EvernoteRepositoryImpl implements EvernoteRepository {
    private static final String AUTH_TOKEN = "S=s31:U=5a2e79:E=172dabeb71a:C=172b6b231d8:P=1cd:A=en-devtoken:V=2:H=8d10a1f653a40a619a74e89fb73b923c";
    private static final String NOTE_STORE_URL = "https://app.yinxiang.com/shard/s31/notestore";
    private static final String USER_AGENT = "JamesLee/EverNoteMap/1.0.0";

    private static final String CONSUMER_KEY = "";

    private NoteStore.Client noteStore;

    private UserStore.Client userStore;

    public EvernoteRepositoryImpl() throws TTransportException {
        THttpClient noteStoreTrans = new THttpClient(NOTE_STORE_URL);
        noteStoreTrans.setCustomHeader("User-Agent", USER_AGENT);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        noteStore = new NoteStore.Client(noteStoreProt, noteStoreProt);
        userStore = new UserStore.Client(noteStoreProt, noteStoreProt);
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

    @Override
    public Note saveNote(Note note) {
        try {
            if (StringUtils.isEmpty(note.getGuid())) {
                return noteStore.createNote(AUTH_TOKEN, note);
            } else {
                return noteStore.updateNote(AUTH_TOKEN, note);
            }
        } catch (EDAMUserException edue) {
            // Something was wrong with the note data
            // See EDAMErrorCode enumeration for error code explanation
            // http://dev.evernote.com/documentation/reference/Errors.html#Enum_EDAMErrorCode
            log.error("EDAMUserException, errorCode:" + edue.getErrorCode() + ",message:" + edue.getParameter(), edue);
            return null;
        } catch (EDAMNotFoundException ednfe) {
            // Parent Notebook GUID doesn't correspond to an actual notebook
            log.error("EDAMNotFoundException: Invalid parent notebook GUID", ednfe);
            return null;
        } catch (Exception e) {
            // Other unexpected exceptions
            log.error("", e);
            return null;
        }
    }

    @Override
    public User getUser(String accessToken) {
        try {
            return userStore.getUser(accessToken);
        } catch (EDAMUserException e) {
            log.error("EDAMUserException:" + e.getMessage(), e);
        } catch (EDAMSystemException e) {
            log.error("EDAMSystemException:" + e.getMessage(), e);
        } catch (TException e) {
            log.error("TException:" + e.getMessage(), e);
        }
        return null;
    }
}
