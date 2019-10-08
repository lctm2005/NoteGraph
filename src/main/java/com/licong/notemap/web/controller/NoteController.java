package com.licong.notemap.web.controller;

import com.licong.notemap.service.NoteService;
import com.licong.notemap.service.domain.Note;
import com.licong.notemap.util.CollectionUtils;
import com.licong.notemap.web.vo.note.NoteParam;
import com.licong.notemap.web.vo.note.NoteResource;
import com.licong.notemap.web.vo.note.NoteResourceAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteResourceAssembler noteResourceAssembler;

    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.GET)
    public NoteResource get(@PathVariable("note_id") UUID noteId) {
        Optional<Note> noteOptional = noteService.findById(noteId);
        if (!noteOptional.isPresent()) {
            throw new ResourceNotFoundException("Not found note which's id is " + noteId);
        }
        return noteResourceAssembler.toResource(noteOptional.get());
    }

    @RequestMapping(value = "/api/note", method = RequestMethod.POST)
    public NoteResource create(@RequestBody NoteParam noteParam) {
        return noteResourceAssembler.toResource(noteService.save(noteParam.toNote()));
    }

    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.PUT)
    public NoteResource update(@PathVariable("note_id") UUID noteId, @RequestBody NoteParam noteParam) {
        Note note = noteParam.toNote();
        note.setId(noteId);
        return noteResourceAssembler.toResource(noteService.save(note));
    }

    @RequestMapping(value = "/api/note/{note_id}", method = RequestMethod.DELETE)
    public NoteResource delete(@PathVariable("note_id") UUID noteId) {
        Optional<Note> noteOptional = noteService.delete(noteId);
        if (noteOptional.isPresent()) {
            return noteResourceAssembler.toResource(noteOptional.get());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/api/note/search/findByTitleContains", method = RequestMethod.GET)
    public PagedResources<NoteResource> findByTitleContains(@RequestParam(value = "title", required = false) String title,
                                                            @PageableDefault Pageable pageable,
                                                            PagedResourcesAssembler assembler,
                                                            NoteResourceAssembler noteResourceAssembler) {
        Page<Note> page = noteService.findByTitleContains(title, pageable);
        if (page.isEmpty()) {
            return assembler.toResource(page);
        }
        Page<NoteResource> noteResourcePage = page.map(e -> noteResourceAssembler.toResource(e));
        return assembler.toResource(noteResourcePage);
    }

    @RequestMapping(value = "/api/note/{note_id}/neighbours", method = RequestMethod.GET)
    public List<NoteResource> neighbours(@PathVariable("note_id") UUID noteId) {
        List<Note> notes = noteService.neighbours(noteId);
        if (CollectionUtils.isEmpty(notes)) {
            return Collections.emptyList();
        } else {
           return notes.stream().map(e -> noteResourceAssembler.toResource(e)).collect(Collectors.toList());
        }
    }
}
