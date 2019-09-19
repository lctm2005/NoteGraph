package com.licong.notemap.web.vo.note;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by lctm2005 on 2017/4/9.
 */
@Data
public class LinkResource extends ResourceSupport {
    private String title;
    private NoteResource start;
    private NoteResource end;

}
