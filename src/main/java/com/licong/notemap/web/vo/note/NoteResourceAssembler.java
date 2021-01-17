package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.web.controller.NoteController;
import com.licong.notemap.web.controller.PageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Component
public class NoteResourceAssembler {

    public NoteResource toResource(Note note) {
        try {
            NoteResource noteResource = new NoteResource(note);
            Method editMethod = PageController.class.getMethod("edit", UUID.class);
            Method getMethod = NoteController.class.getMethod("get", UUID.class);
            Method neighbours = NoteController.class.getMethod("neighbours", UUID.class);
            noteResource.add(linkTo(editMethod, note.getId()).withRel("edit"));
            noteResource.add(linkTo(getMethod, note.getId()).withRel(Link.REL_SELF));
            noteResource.add(linkTo(neighbours, note.getId()).withRel("neighbours"));
            List<Note> refs = note.getReference();
            if(CollectionUtils.isEmpty(refs)) {
                return noteResource;
            }
            for(Note ref : refs) {
                noteResource.addRef(new NoteResource(ref));
            }
            return noteResource;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
