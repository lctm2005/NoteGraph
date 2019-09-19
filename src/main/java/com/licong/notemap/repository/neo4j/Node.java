package com.licong.notemap.repository.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.typeconversion.UuidStringConverter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NodeEntity(label = Node.LABLE)
public class Node {

    public static final String LABLE = "NOTE";

    @Id
    @GeneratedValue
    private Long id;

    @Property
    @Index(unique = true)
    private String uniqueId;

    @Property
    private String title;
}
