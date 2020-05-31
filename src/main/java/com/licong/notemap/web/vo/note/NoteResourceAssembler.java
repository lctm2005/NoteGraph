package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.Note;
import com.licong.notemap.web.controller.NoteController;
import com.licong.notemap.web.controller.PageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@Component
public class NoteResourceAssembler extends ResourceAssemblerSupport<Note, NoteResource> {

    public NoteResourceAssembler() {
        super(NoteController.class, NoteResource.class);
    }

    @Override
    public NoteResource toResource(Note entity) {
        try {
            NoteResource noteResource = new NoteResource();
            noteResource.setNoteId(entity.getId());
            noteResource.setTitle(entity.getTitle());
            noteResource.setMarkdown(entity.getMarkdown());
            Method editMethod = PageController.class.getMethod("edit", UUID.class);
            Method getMethod = NoteController.class.getMethod("get", UUID.class);
            noteResource.add(linkTo(editMethod, entity.getId()).withRel("edit"));
            noteResource.add(linkTo(getMethod, entity.getId()).withRel(Link.REL_SELF));
            return noteResource;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
