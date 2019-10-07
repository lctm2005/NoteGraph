package com.licong.notemap.repository.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class NoteContent {

    @Id
    private UUID uuid;

    private String markdown;
}
