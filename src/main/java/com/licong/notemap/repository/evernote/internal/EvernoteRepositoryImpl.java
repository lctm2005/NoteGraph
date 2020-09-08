package com.licong.notemap.repository.evernote.internal;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.protocol.TBinaryProtocol;
import com.evernote.thrift.transport.THttpClient;
import com.evernote.thrift.transport.TTransportException;
import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.util.StringUtils;
import com.licong.notemap.web.security.evernote.EvernoteAuthenticationConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.licong.notemap.web.security.evernote.EvernoteAuthenticationConstant.APP_NOTEBOOK_NAME;

/**
 * Created by lctm2005 on 2017/4/20.
 */
@Slf4j
@Repository
public class EvernoteRepositoryImpl implements EvernoteRepository {

    public Note get(UUID noteId, String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.getNote(accessToken, noteId.toString(), true, true, true, true);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public SyncState getSyncState(String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.getSyncState(accessToken);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public NoteList findNotes(NoteFilter noteFilter, Integer offset, Integer limit, String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.findNotes(accessToken, noteFilter, offset, limit);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public String getNoteContent(UUID noteId, String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.getNoteContent(accessToken, noteId.toString());
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public Note saveNote(Note note, String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            if (StringUtils.isEmpty(note.getGuid())) {
                return noteStore.createNote(accessToken, note);
            } else {
                return noteStore.updateNote(accessToken, note);
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
    public List<Notebook> findNotebooks(String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.listNotebooks(accessToken);
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Notebook createAppNotebook(String noteStoreUrl, String accessToken) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            Notebook notebook = new Notebook();
            notebook.setName(APP_NOTEBOOK_NAME);
            return noteStore.createNotebook(accessToken, notebook);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public Notebook getNotebook(String noteStoreUrl, String accessToken, String guid) {
        try {
            NoteStore.Client noteStore = buildNoteStore(noteStoreUrl);
            return noteStore.getNotebook(accessToken, guid);
        } catch (EDAMNotFoundException e) {
            // 回到授权过程，以便用户选择新的笔记本
            // 再通过 listNotebooks 获取新笔记本的 GUID
            log.error("", e);
            throw new AuthenticationServiceException(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            throw new AuthenticationServiceException(e.getMessage());
        }
    }


    private NoteStore.Client buildNoteStore(String noteStoreUrl) throws TTransportException {
        TBinaryProtocol tBinaryProtocol = buildTBinaryProtocol(noteStoreUrl);
        return new NoteStore.Client(tBinaryProtocol, tBinaryProtocol);
    }

    private TBinaryProtocol buildTBinaryProtocol(String noteStoreUrl) throws TTransportException {
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        noteStoreTrans.setCustomHeader("User-Agent", EvernoteAuthenticationConstant.USER_AGENT);
        return new TBinaryProtocol(noteStoreTrans);
    }

}
