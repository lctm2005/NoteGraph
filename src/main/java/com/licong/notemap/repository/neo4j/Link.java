package com.licong.notemap.repository.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@RelationshipEntity
public class Link {
    @Id
    @GeneratedValue
    private Long id;

    @Property
    private String title;

    @StartNode
    private Node start;

    @EndNode
    private Node end;
}
