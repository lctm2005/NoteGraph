package com.licong.notemap.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 笔记连接解析器
 */
public abstract class NoteInnerLinkUtils {


    public final static String START = "@\\[";
    public final static String MIDDLE = "](/note/";
    public final static String ID_END = ")";


    public static List<NoteInnerLink> parse(String content) {
        List<NoteInnerLink> result = new ArrayList<>();
        String[] links = content.split(START);
        if (ArrayUtils.isNotEmpty(links)) {
            for (String link : links) {
                int middleIndex = link.indexOf(MIDDLE);
                if (-1 == middleIndex) {
                    continue;
                }
                String title = link.substring(0, middleIndex);
                String uuid = link.substring(middleIndex + MIDDLE.length(), link.indexOf(ID_END));
                NoteInnerLink noteInnerLink = new NoteInnerLink(title, UUID.fromString(uuid));
                result.add(noteInnerLink);
            }
        }
        return result;
    }

    @Data
    @AllArgsConstructor
    public static class NoteInnerLink {
        private String title;
        private UUID noteId;
    }
}
