package com.licong.notemap.service.domain;

import com.licong.notemap.repository.mongo.NoteContent;
import com.licong.notemap.repository.neo4j.Node;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Note {
    private UUID id;
    private String title;
    private String content;


    public Note(Node node) {
        this.id = UUID.fromString(node.getUniqueId());
        this.title = node.getTitle();
        this.content = "";
    }

    public Note(Node node, NoteContent noteContent) {
        this.id = UUID.fromString(node.getUniqueId());
        this.title = node.getTitle();
        this.content = null == noteContent ? "" : noteContent.getMarkdown();
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
