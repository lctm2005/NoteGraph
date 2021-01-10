package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;


@Data
public class NoteParam {
    private String title;
    private String markdown;

    public Note toNote() {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(markdown);
        return note;
    }
}
