package com.licong.notemap.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licong.notemap.domain.Note;
import com.licong.notemap.domain.Link;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.LinkService;
import com.licong.notemap.web.vo.GraphVo;
import com.licong.notemap.web.vo.NodeVo;
import com.licong.notemap.web.vo.LinkVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Slf4j
@Controller
public class GraphController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private LinkService linkService;

    @RequestMapping("/")
    public ModelAndView welcome() throws Exception {

        Iterable<Note> notes = noteService.findAll();
        List<NodeVo> nodeVos = new ArrayList<>();
        for (Note note : notes) {
            nodeVos.add(NodeVo.convert(note));
        }

        Iterable<Link> links = linkService.findAll();
        List<LinkVo> linkVos = new ArrayList<>();
        for (Link link : links) {
            linkVos.add(LinkVo.convert(link));
        }

        GraphVo graphVo = new GraphVo();
        graphVo.setNodes(nodeVos);
        graphVo.setLinks(linkVos);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        ObjectMapper objectMapper = new ObjectMapper();
        modelAndView.addObject("graph", objectMapper.writer().writeValueAsString(graphVo));
        return modelAndView;
    }
}
