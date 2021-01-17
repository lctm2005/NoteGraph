package com.licong.notemap.web.vo.tag;

import com.licong.notemap.service.domain.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TagResource extends EntityModel {

    private UUID tagId;
    private String title;

    public TagResource(Tag tag) {
        this.tagId = tag.getId();
        this.title = tag.getTitle();
    }
}
