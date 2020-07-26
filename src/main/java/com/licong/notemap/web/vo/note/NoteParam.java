package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;
import org.apache.commons.text.StringEscapeUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Valid
public class NoteParam {
    @NotEmpty
    private String title;
    @NotEmpty
    private String markdown;

    public Note toNote() {
        Note note = new Note();
        note.setTitle(title);
        note.setMarkdown(markdown);
        return note;
    }
}
