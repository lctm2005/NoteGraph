package com.licong.notemap.util.filemanager;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * Created by lctm2005 on 2015/12/23.
 */
@Data
public class SingleFileSearchResult {
    private File file;
    private int count;
    private List<String> lines;

    public void setLine(String line) {
        lines.add(line);
    }
}
