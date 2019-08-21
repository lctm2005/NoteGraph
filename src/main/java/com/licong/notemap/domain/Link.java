package com.licong.notemap.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RelationshipEntity
public class Link {
    @Id
    @GeneratedValue
    private Long id;

    @Property
    private String title;

    @StartNode
    private Note start;

    @EndNode
    private Note end;
}
