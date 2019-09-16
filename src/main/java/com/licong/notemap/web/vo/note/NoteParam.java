package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NoteParam {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    public Note toNote() {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        return note;
    }
}
