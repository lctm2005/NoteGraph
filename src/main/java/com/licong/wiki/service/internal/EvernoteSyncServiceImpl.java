package com.licong.wiki.service.internal;

import com.licong.wiki.service.EvernoteSyncService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/18.
 */
@Service
public class EvernoteSyncServiceImpl implements EvernoteSyncService {

    @Override
    public void syncNote(Long userId, UUID noteId) {

    }

    @Override
    public void syncNoteBook(Long userId, UUID noteId) {

    }
}
