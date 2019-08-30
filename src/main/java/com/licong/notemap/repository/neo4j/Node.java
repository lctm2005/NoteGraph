package com.licong.notemap.repository.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.typeconversion.UuidStringConverter;

import java.util.UUID;

@Data
@NodeEntity(label = "Note")
public class Node {
    @Id
    @GeneratedValue
    private Long id;

    @Property
    @Convert(UuidStringConverter.class)
    private UUID uuid;

    @Property
    private String title;
}
