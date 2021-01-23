package com.licong.notemap.web.controller;

import com.licong.notemap.util.JsonUtils;
import com.licong.notemap.util.StringUtils;
import com.licong.notemap.web.vo.note.NoteResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Controller
public class PageController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("graph");
        return modelAndView;
    }

    @RequestMapping("/graph/{tag}")
    public ModelAndView graph(@PathVariable(value = "tag") String tag) {
        ModelAndView modelAndView = new ModelAndView();
        if (StringUtils.isNotEmpty(tag)) {
            modelAndView.addObject("tag", tag);
        } else {
            modelAndView.addObject("tag", "");
        }
        modelAndView.setViewName("graph");
        return modelAndView;
    }

    @RequestMapping("/note/{note_id}")
    public ModelAndView edit(@PathVariable(value = "note_id") UUID noteId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note");
        modelAndView.addObject("note_id", noteId);
        return modelAndView;
    }

    @RequestMapping("/note")
    public ModelAndView newNote() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note");
        modelAndView.addObject("note", JsonUtils.toJson(new NoteResource()));
        return modelAndView;
    }

    @RequestMapping("/financial")
    public ModelAndView financial() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("financial");
        return modelAndView;
    }

    @RequestMapping("/label")
    public ModelAndView label() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("label");
        return modelAndView;
    }
}
