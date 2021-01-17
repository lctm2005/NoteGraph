package com.licong.notemap.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 笔记连接解析器
 */
public abstract class NoteInnerLinkUtils {

    private final static int START_INDEX = 2;
    private final static String MIDDLE_LABEL = "](/note/";
    private final static String END_LABEL = ")";


    public static List<NoteInnerLink> parse(String content) {
        if (StringUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        List<NoteInnerLink> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("@\\[.*?\\]\\((\\/note\\/).*?\\)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String extract = matcher.group(0);
            int middleIndex = extract.indexOf(MIDDLE_LABEL);

            String title = extract.substring(START_INDEX, middleIndex);
            String uuid = extract.substring(middleIndex + MIDDLE_LABEL.length(), extract.length() - 1);
            NoteInnerLink noteInnerLink = new NoteInnerLink(title, UUID.fromString(uuid));
            System.out.println(noteInnerLink);
            result.add(noteInnerLink);

        }
        return result;
    }

    @Data
    @AllArgsConstructor
    public static class NoteInnerLink {
        private String title;
        private UUID noteId;

        @Override
        public String toString() {
            return "NoteInnerLink{" +
                    "title='" + title + '\'' +
                    ", noteId=" + noteId +
                    '}';
        }
    }
}
