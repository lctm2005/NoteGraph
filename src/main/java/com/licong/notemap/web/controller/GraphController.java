package com.licong.notemap.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.LinkService;
import com.licong.notemap.service.domain.NoteLink;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.util.JsonUtils;
import com.licong.notemap.web.vo.graph.GraphVo;
import com.licong.notemap.web.vo.graph.NodeVo;
import com.licong.notemap.web.vo.graph.LinkVo;
import com.licong.notemap.web.vo.note.NoteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class GraphController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private LinkService linkService;

    @RequestMapping("/")
    public ModelAndView graph() throws Exception {
        List<Note> notes = noteService.findAll();
        List<NodeVo> nodeVos = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(notes)) {
            nodeVos = notes.stream().map(e -> NodeVo.convert(e)).collect(Collectors.toList());
        }
        List<NoteLink> noteLinks = linkService.findAll();
        List<LinkVo> linkVos = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(noteLinks)) {
            linkVos = noteLinks.stream().map(e -> LinkVo.convert(e)).collect(Collectors.toList());
        }
        GraphVo graphVo = new GraphVo();
        graphVo.setNodes(nodeVos);
        graphVo.setLinks(linkVos);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("graph");
        ObjectMapper objectMapper = new ObjectMapper();
        modelAndView.addObject("graph", objectMapper.writer().writeValueAsString(graphVo));
        return modelAndView;
    }

    @RequestMapping("/note/{note_id}")
    public ModelAndView note(@PathVariable(value = "note_id") UUID noteId) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note");
        modelAndView.addObject("note_id", noteId);
        return modelAndView;
    }

    @RequestMapping("/note")
    public ModelAndView newNote() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note");
        modelAndView.addObject("note", JsonUtils.toJson(new NoteVo()));
        return modelAndView;
    }

}
