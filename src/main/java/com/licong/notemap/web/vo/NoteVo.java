package com.licong.notemap.web.vo;

import com.licong.notemap.domain.Note;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * {category:2, name: '龙-韦恩',value : 1}
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class NoteVo {

    private static final String HREF_PREFIX = "https://app.yinxiang.com/shard/s31/nl/5910137/";
    private String name;
    private Integer category;
    private String href;
    private Integer value;
    private List<Integer> symbolSize;

    public static NoteVo convert(Note note) {
        NoteVo noteVo = new NoteVo();
        noteVo.setName(note.getTitle());
        noteVo.setCategory(0);
        // https://[service]/shard/[shardId]/nl/[userId]/[noteGuid]
        List<Integer> symbolSize = new ArrayList<>();
        symbolSize.add(note.getTitle().length() * 8);
        symbolSize.add(10);
        noteVo.setSymbolSize(symbolSize);
        noteVo.setHref(HREF_PREFIX + note.getUuid());
        noteVo.setValue(note.getTitle().length() * 2);
        return noteVo;
    }
}
