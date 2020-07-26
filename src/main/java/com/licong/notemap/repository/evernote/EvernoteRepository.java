package com.licong.notemap.repository.evernote;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.User;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/20.
 */
public interface EvernoteRepository {

    Note get(UUID noteId);

    SyncState getSyncState();

    NoteList findNotes(NoteFilter noteFilter, Integer offset, Integer limit);

    String getNoteContent(UUID noteId);

    Note saveNote(Note note);

    User getUser(String accessToken);
}
