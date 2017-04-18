package com.licong.wiki.web.vo;

import com.licong.wiki.domain.Note;
import lombok.Data;

/**
 * {category:2, name: '龙-韦恩',value : 1}
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class NodeVo {

    private static final String HREF_PREFIX = "https://app.yinxiang.com/shard/s31/nl/5910137/";
    private String name;
    private Integer category;
    private String href;

    public static NodeVo convert(com.evernote.edam.type.Note note) {
        NodeVo node = new NodeVo();
        node.setName(note.getTitle());
        node.setCategory(0);
        // https://[service]/shard/[shardId]/nl/[userId]/[noteGuid]
        node.setHref("https://app.yinxiang.com/shard/s31/nl/5910137/" + note.getGuid());
        return node;
    }

    public static NodeVo convert(Note note) {
        NodeVo nodeVo = new NodeVo();
        nodeVo.setName(note.getName());
        nodeVo.setCategory(0);
        // https://[service]/shard/[shardId]/nl/[userId]/[noteGuid]
        nodeVo.setHref(HREF_PREFIX + note.getUuid());
        return nodeVo;
    }
}
