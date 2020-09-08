package com.licong.notemap.service;

import com.evernote.edam.type.Note;
import com.licong.notemap.web.security.evernote.EvernoteAccessToken;

public interface EverNoteService {
    /**
     * 保存到印象笔记
     *
     * @param everNoteId 印象笔记ID
     * @param note       笔记
     * @return
     */
    Note save(String everNoteId, com.licong.notemap.service.domain.Note note, EvernoteAccessToken evernoteAccessToken);
}
