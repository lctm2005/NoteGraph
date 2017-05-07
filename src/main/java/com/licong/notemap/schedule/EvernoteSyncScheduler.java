package com.licong.notemap.schedule;

import com.licong.notemap.service.EvernoteSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by lctm2005 on 2017/4/22.
 */
@Component
@Configurable
@EnableScheduling
public class EvernoteSyncScheduler {

    @Autowired
    private EvernoteSyncService evernoteSyncService;

    // 1 minute
    @Scheduled(fixedRate = 1000 * 60)
    public void sync(){
        evernoteSyncService.syncNoteBook(0L, UUID.fromString("dea6691f-cd37-40f9-abbd-b3785da21636"));
    }
}
