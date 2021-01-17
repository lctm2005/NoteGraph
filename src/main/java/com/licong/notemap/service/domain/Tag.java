package com.licong.notemap.service.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.UUID;

@Data
@org.springframework.data.neo4j.core.schema.Node(Tag.LABLE)
public class Tag {

    public static final String LABLE = "TAG";

    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private UUID id;

    @Property
    private String title;

}
