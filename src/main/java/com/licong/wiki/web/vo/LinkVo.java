package com.licong.wiki.web.vo;

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
    private Integer weight;
}
