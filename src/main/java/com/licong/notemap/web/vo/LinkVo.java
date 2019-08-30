package com.licong.notemap.web.vo;

import com.licong.notemap.repository.neo4j.Link;
import lombok.Data;

/**
 * {source : '丽萨-乔布斯', target : '乔布斯', weight : 1, name: '女儿'}
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class LinkVo {
    private String name;
    private String source;
    private String target;

    public static LinkVo convert(Link link) {
        LinkVo linkVo = new LinkVo();
        linkVo.setName(link.getTitle());
        linkVo.setSource(link.getStart().getTitle());
        linkVo.setTarget(link.getEnd().getTitle());
        return linkVo;
    }
}
