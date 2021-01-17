package com.licong.notemap.web.vo.tag;

import com.licong.notemap.service.domain.Tag;
import com.licong.notemap.web.controller.NoteController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Component
public class TagResourceAssembler {

    public TagResource toResource(Tag tag) {
        try {
            TagResource tagResource = new TagResource(tag);
            Method findNotes = NoteController.class.getMethod("findByTitleContains", String.class,
                    Pageable.class,
                    PagedResourcesAssembler.class,
                    TagResourceAssembler.class);
            tagResource.add(linkTo(findNotes, tagResource.getTitle()).withRel("findNotes"));
            return tagResource;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
