package com.licong.notemap.service;

import com.licong.notemap.domain.NoteContent;

import java.util.Optional;
import java.util.UUID;

public interface NoteContentService {
    Optional<NoteContent> findById(UUID uuid);
}
