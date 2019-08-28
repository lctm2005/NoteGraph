package com.licong.notemap.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class NoteContent {

    @Id
    @Indexed(unique = true)
    private UUID uuid;

    private String markdown;
}
