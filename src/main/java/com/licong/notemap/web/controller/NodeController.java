package com.licong.notemap.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licong.notemap.domain.Link;
import com.licong.notemap.domain.Note;
import com.licong.notemap.service.LinkService;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.web.vo.LinkVo;
import com.licong.notemap.web.vo.NodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Slf4j
@Controller
public class NodeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private LinkService linkService;

    @RequestMapping("/")
    public ModelAndView welcome() throws Exception {

        List<Note> notes  = noteService.findAll();
        List<NodeVo> nodeVos = new ArrayList<>();
        Map<UUID, Note> noteMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(notes)) {
            for(Note note : notes) {
                nodeVos.add(NodeVo.convert(note));
                noteMap.put(note.getUuid(), note);
            }
        }

        List<Link> links  =linkService.findAll();
        List<LinkVo> linkVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(notes)) {
            for(Link link : links) {
                try {
                    linkVos.add(LinkVo.convert(link, noteMap));
                }catch (Exception e) {
                    log.error("", e);
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        ObjectMapper objectMapper = new ObjectMapper();
        modelAndView.addObject("nodes", objectMapper.writer().writeValueAsString(nodeVos));
        modelAndView.addObject("links", objectMapper.writer().writeValueAsString(linkVos));
        return modelAndView;
    }





}
