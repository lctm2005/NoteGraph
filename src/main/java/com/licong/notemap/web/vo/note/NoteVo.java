package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteVo {

    private UUID id;
    private String title;
    private String content;

    public NoteVo(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
    }
}
