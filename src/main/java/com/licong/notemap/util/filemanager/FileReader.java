package com.licong.notemap.util.filemanager;


import com.licong.notemap.util.StringUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by lctm2005 on 2015/12/27.
 */
@Slf4j
public class FileReader {


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName    文件名
     * @param endOfLine  行末结束符(可选)
     * @return
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static String readByLine(String fileName, String endOfLine) {
        if ((StringUtils.isEmpty(fileName))) {
            throw new IllegalArgumentException("Param[fileName] couldn't be empty.");
        }
        boolean hasEndOfLine = StringUtils.isNotEmpty(endOfLine);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString);
                if(hasEndOfLine) {
                    stringBuilder.append(endOfLine);
                }
            }
            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error("", e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    log.error("", e1);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(readByLine("J://Git//信@!#信.txt", "\n"));
    }
}
