package com.licong.notemap.service.internal;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.service.EverNoteService;
import com.licong.notemap.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

@Slf4j
@Service
public class EverNoteServiceImpl implements EverNoteService {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
    private static final String EN_NOTE_START = "<en-note>";
    private static final String CENTER_START =
            "<center style=\"display:none !important;visibility:collapse !important;height:0 !important;white-space:nowrap;width:100%;overflow:hidden\">";
    private static final String CENTER_END = "</center>";
    private static final String EN_NOTE_END = "</en-note>";

    @Autowired
    private EvernoteRepository evernoteRepository;

    public com.evernote.edam.type.Note save(String everNoteId, com.licong.notemap.service.domain.Note note)  {
        // 判断是否存在EverNote，不存在创建，存在更新
        Note everNote;
        if (StringUtils.isNotEmpty(everNoteId)) {
            everNote = evernoteRepository.get(UUID.fromString(everNoteId));
            if (null == everNote || !everNote.isActive()) {
                everNote = new Note();
            }
        } else {
            everNote = new Note();
        }
        updateEverNoteContent(note, everNote);
        updateEverNoteAttribute(everNote);
        return evernoteRepository.saveNote(everNote);
    }


    /**
     * 更新印象笔记内容
     *
     * @param everNote 印象笔记
     */
    private void updateEverNoteAttribute(Note everNote) {
        NoteAttributes noteAttributes = everNote.getAttributes();
        noteAttributes.setContentClass("yinxiang.markdown");
        everNote.setAttributes(noteAttributes);
    }

    /**
     * 更新印象笔记内容
     *
     * @param note     笔记
     * @param everNote 印象笔记
     */
    private void updateEverNoteContent(com.licong.notemap.service.domain.Note note, Note everNote)  {
        everNote.setTitle(note.getTitle());
        try {
            everNote.setContent(HEADER
                    + EN_NOTE_START
                    + CENTER_START
                    + URLEncoder.encode(note.getMarkdown(), "UTF-8").replace( "+", "%20" ).replace("*", "%2A").replace( "%7E", "~" )
                    + CENTER_END
                    + EN_NOTE_END);
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
