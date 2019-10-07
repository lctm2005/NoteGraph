package com.licong.notemap.web.vo.note;

import com.licong.notemap.service.domain.NoteLink;
import com.licong.notemap.web.controller.LinkController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkResourceAssembler extends ResourceAssemblerSupport<NoteLink, LinkResource> {

    public LinkResourceAssembler() {
        super(LinkController.class, LinkResource.class);
    }

    @Autowired
    private NoteResourceAssembler noteResourceAssembler;

    @Override
    public LinkResource toResource(NoteLink entity) {
        LinkResource linkResource = new LinkResource();
        linkResource.setLinkId(entity.getId());
        linkResource.setStart(noteResourceAssembler.toResource(entity.getStart()));
        linkResource.setTitle(entity.getTitle());
        linkResource.setEnd(noteResourceAssembler.toResource(entity.getEnd()));
        return linkResource;
    }
}
