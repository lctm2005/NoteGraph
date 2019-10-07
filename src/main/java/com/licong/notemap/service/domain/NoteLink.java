package com.licong.notemap.service.domain;

import com.licong.notemap.repository.neo4j.Link;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class NoteLink {

    private Long id;

    private String title;

    private Note start;

    private Note end;

    public static NoteLink convert(Link link) {
        NoteLink noteLink = new NoteLink();
        noteLink.setStart(new Note(link.getStart()));
        noteLink.setTitle(link.getTitle());
        noteLink.setEnd(new Note(link.getEnd()));
        noteLink.setId(link.getId());
        return noteLink;
    }

}
