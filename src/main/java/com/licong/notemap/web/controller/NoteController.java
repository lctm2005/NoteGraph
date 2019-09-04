package com.licong.notemap.web.controller;

import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.web.vo.NoteParam;
import com.licong.notemap.web.vo.NoteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    @ResponseBody
    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.GET)
    public NoteVo get(@PathVariable("note_id") UUID noteId) {
        Optional<Note> noteOptional = noteService.findById(noteId);
        if (!noteOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found note which's id is " + noteId);
        }
        return new NoteVo(noteOptional.get());
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

    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.DELETE)
    public NoteVo delete(@PathVariable("note_id") UUID noteId) {
        Optional<Note> noteOptional = noteService.delete(noteId);
        if (noteOptional.isPresent()) {
            return new NoteVo(noteOptional.get());
        } else {
            return null;
        }
    }
}
