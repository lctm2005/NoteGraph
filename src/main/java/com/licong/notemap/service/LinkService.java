package com.licong.notemap.service;

import com.licong.notemap.service.domain.NoteLink;

import java.util.List;
import java.util.UUID;

public interface LinkService {

    List<NoteLink> findAll();

    List<NoteLink> findByNotes(List<UUID> noteIds);

    void rebuild();
}
