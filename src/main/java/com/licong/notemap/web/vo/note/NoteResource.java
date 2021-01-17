package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class NoteResource extends EntityModel {

    private UUID noteId;
    private String title;
    private String markdown;
    private List<NoteResource> refs = new ArrayList<>();

    public NoteResource(Note note) {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.markdown = note.getContent();
    }

    public void addRef(NoteResource noteResource) {
        refs.add(noteResource);
    }

}
