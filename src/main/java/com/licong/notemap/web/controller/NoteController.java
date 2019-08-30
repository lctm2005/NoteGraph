package com.licong.notemap.web.controller;

import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.web.vo.NoteParam;
import com.licong.notemap.web.vo.NoteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    @ResponseBody
    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.GET)
    public NoteVo get(@PathVariable("note_id") UUID noteId) {
        Note note = noteService.findById(noteId);
        if (null == note) {
            return null;
        }
        return new NoteVo(note);
    }

    @RequestMapping(value = "/api/note", method = RequestMethod.POST)
    public NoteVo create(@RequestBody NoteParam noteParam) {
        return new NoteVo(noteService.save(noteParam.toNote()));
    }

    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.PUT)
    public NoteVo update(@PathVariable("note_id") UUID noteId, @RequestBody NoteParam noteParam) {
        Note note = noteParam.toNote();
        note.setId(noteId);
        return new NoteVo(noteService.save(note));
    }

}
