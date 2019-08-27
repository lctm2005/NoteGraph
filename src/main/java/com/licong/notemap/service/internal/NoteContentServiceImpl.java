package com.licong.notemap.service.internal;

import com.licong.notemap.domain.NoteContent;
import com.licong.notemap.repository.mongo.NoteContentRepository;
import com.licong.notemap.service.NoteContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NoteContentServiceImpl implements NoteContentService {
    @Autowired
    private NoteContentRepository noteContentRepository;

    @Override
    public Optional<NoteContent> findById(UUID uuid) {
        return noteContentRepository.findById(uuid);
    }
}
