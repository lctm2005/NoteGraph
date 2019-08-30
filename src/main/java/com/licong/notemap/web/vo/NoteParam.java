package com.licong.notemap.web.vo;

import com.licong.notemap.service.domain.Note;
import lombok.Data;

@Data
public class NoteParam {
    private String title;
    private String content;

    public Note toNote() {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        return note;
    }
}
