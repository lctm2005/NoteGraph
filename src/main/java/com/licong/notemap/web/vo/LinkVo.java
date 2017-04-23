package com.licong.notemap.web.vo;

import com.licong.notemap.domain.Link;
import com.licong.notemap.domain.Note;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * {source : '丽萨-乔布斯', target : '乔布斯', weight : 1, name: '女儿'}
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class LinkVo {
    private String name;
    private String source;
    private String target;

    public static LinkVo convert(Link link, Map<UUID, Note> noteMap) {
        LinkVo linkVo = new LinkVo();
        linkVo.setName(link.getName());
        linkVo.setSource(noteMap.get(link.getSource()).getName());
        linkVo.setTarget(noteMap.get(link.getTarget()).getName());
        return linkVo;
    }
}
