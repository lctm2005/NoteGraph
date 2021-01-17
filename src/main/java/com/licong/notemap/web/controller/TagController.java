package com.licong.notemap.web.controller;

import com.licong.notemap.service.TagService;
import com.licong.notemap.service.domain.Tag;
import com.licong.notemap.web.vo.tag.TagResource;
import com.licong.notemap.web.vo.tag.TagResourceAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/api/tags/search/findByTitleContains", method = RequestMethod.GET)
    public PagedModel<TagResource> findByTitleContains(@RequestParam(value = "title", required = false) String title,
                                                       @PageableDefault Pageable pageable,
                                                       PagedResourcesAssembler assembler,
                                                       TagResourceAssembler tagResourceAssembler) {
        Page<Tag> page = tagService.findByTitleContains(title, pageable);
        if (page.isEmpty()) {
            return assembler.toModel(page);
        }
        Page<TagResource> tagResourcePage = page.map(e -> tagResourceAssembler.toResource(e));
        return assembler.toModel(tagResourcePage);
    }




}
