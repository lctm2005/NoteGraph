package com.licong.notemap.service;

import com.evernote.edam.type.Note;

import java.io.UnsupportedEncodingException;

public interface EverNoteService {
    /**
     * 保存到印象笔记
     * @param everNoteId  印象笔记ID
     * @param note        笔记
     * @return
     */
    Note save(String everNoteId, com.licong.notemap.service.domain.Note note) ;
}
