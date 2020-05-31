package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteResource extends ResourceSupport {

    private UUID noteId;
    private String title;
    private String markdown;

    public NoteResource(Note note) {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.markdown = note.getMarkdown();
    }
}
