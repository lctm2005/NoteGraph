package com.licong.notemap.web.vo;

import com.licong.notemap.service.domain.Note;
import lombok.Data;

/**
 * {category:2, name: '龙-韦恩',value : 1}
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class NodeVo {

    private static final String HREF_PREFIX = "/note/";
    private String name;
    private String href;
    private Integer value;

    public static NodeVo convert(Note note) {
        NodeVo nodeVo = new NodeVo();
        StringBuilder stringBuilder = new StringBuilder(note.getTitle());
        if (stringBuilder.length() > 6) {
            stringBuilder.insert(6, "\n");
        }
        nodeVo.setName(stringBuilder.toString());
        nodeVo.setHref(HREF_PREFIX + note.getId());
        nodeVo.setValue(note.getTitle().length() * 2);
        return nodeVo;
    }
}
