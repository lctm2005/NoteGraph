package com.licong.notemap.service.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;


@Data
@org.springframework.data.neo4j.core.schema.Node(Note.LABLE)
public class Note {

    public static final String LABLE = "NOTE";

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private String title;

    @Property
    private String content;

    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.OUTGOING)
    private List<Note> reference;

}
