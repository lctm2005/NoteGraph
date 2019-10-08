package com.licong.notemap.web.controller;

import com.licong.notemap.service.LinkService;
import com.licong.notemap.service.domain.NoteLink;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.web.vo.note.LinkResource;
import com.licong.notemap.web.vo.note.LinkResourceAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkResourceAssembler linkResourceAssembler;

    @RequestMapping(value = "/api/link/search/findByStartInOrEndIn", method = RequestMethod.POST)
    public List<LinkResource> get(@RequestBody List<UUID> nodeIds) {
        List<NoteLink> noteLinks = linkService.findByNotes(nodeIds);
        if (CollectionUtils.isEmpty(noteLinks)) {
            return Collections.emptyList();
        }
        return noteLinks.stream().map(e -> linkResourceAssembler.toResource(e)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/link/actions/rebuild", method = RequestMethod.POST)
    public void rebuild() {
        linkService.rebuild();
    }

}
