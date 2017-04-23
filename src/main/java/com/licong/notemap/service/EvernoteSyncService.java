package com.licong.notemap.service;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/18.
 */
public interface EvernoteSyncService {
    /**
     * 同步笔记
     * @param userId  用戶ID
     * @param noteId  笔记ID
     */
    void syncNote(Long userId, UUID noteId);

    /**
     * 同步笔记本
     * @param userId  用戶ID
     * @param noteBookId  笔记本ID
     */
    void syncNoteBook(Long userId, UUID noteBookId);

    /**
     * 同步
     */
    void sync();
}
