package com.licong.notemap.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class NoteContent {
    @Id
    private Long id;

    private UUID uuid;

    private String markdown;
}
