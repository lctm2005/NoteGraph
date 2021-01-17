package com.licong.notemap.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteTagUtils {
    public static List<String> parseTags(String content) {
        Pattern pattern = Pattern.compile("@.+@");
        Matcher matcher = pattern.matcher(content);
        List<String> tags = new ArrayList<>();
        while (matcher.find()) {
            String extract = matcher.group(0);
            tags.add(extract.substring(1, extract.length() - 1));
        }
        return tags;
    }
}
