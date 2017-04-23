package com.licong.notemap.util;

import java.util.ArrayList;
import java.util.List;

/**
 * XML 工具
 * Created by lctm2005 on 2017/4/16.
 */
public class XmlUtils {

    private static final String A_START_TAG = "<a";
    private static final String A_END_TAG = "</a>";
    private static final String A_HREF_START_TAG = "href=\"";
    private static final String A_HREF_END_TAG = "\"";

    /**
     * 递归提取超链接
     * @param content XML內容
     * @param hrefs   超链接列表
     */
    private static void parse(String content, List<Href> hrefs) {
        Integer start = content.indexOf(A_START_TAG);
        Integer end = content.indexOf(A_END_TAG);
        if (-1 != start && -1 != end) {
            Href href = new Href();
            String aStr = content.substring(start, end);
            href.setUrl(aStr.substring(aStr.indexOf(A_HREF_START_TAG) + A_HREF_START_TAG.length(),
                    aStr.indexOf(A_HREF_END_TAG, aStr.indexOf(A_HREF_START_TAG) + A_HREF_START_TAG.length())));
            href.setName(aStr.substring(aStr.indexOf(">") + ">".length()));
            hrefs.add(href);
        } else {
            return;
        }
        String rest = content.substring(end + A_END_TAG.length());
        if (null != rest && rest.length() != 0) {
            parse(rest, hrefs);
        }
    }

    /**
     * 提取超链接
     * @param content XML內容
     * @return 超链接列表
     */
    public static List<Href> extractHrefs(String content) {
        List<Href> hrefs = new ArrayList<>();
        parse(content, hrefs);
        return hrefs;
    }
}
