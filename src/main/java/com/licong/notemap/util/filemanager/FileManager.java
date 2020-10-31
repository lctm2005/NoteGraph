package com.licong.notemap.util.filemanager;

import com.licong.notemap.util.StringUtils;
import com.licong.notemap.util.filemanager.FileReplaceResult;
import com.licong.notemap.util.filemanager.SingleFileSearchResult;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.licong.notemap.util.ObjectUtils.isNotNull;
import static com.licong.notemap.util.ObjectUtils.isNull;


/**
 * 文件管理器
 *
 * @author James-li
 */
@Slf4j
public class FileManager {

    /**
     * 删除文件
     *
     * @param file 待删除文件
     * @return 成功返回true，失败返回false
     */
    public static boolean delete(File file) {
        if (isNull(file)) {
            log.error("file is null");
            return false;
        }
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }


    /**
     * * 创建空文件
     *
     * @param file 待创建文件
     * @return 成功返回true，失败返回false
     */
    public static boolean create(File file) {
        if (isNull(file)) {
            log.error("file is null");
            return false;
        }
        try {
            if (file.exists()) {
                log.error("File is already exist:" + file.getAbsolutePath());
                return false;
            }
            return file.createNewFile();
        } catch (IOException e) {
            log.error("Create file failed", e);
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param file        待创建文件
     * @param inputStream 输入流
     * @return 成功返回true，失败返回false
     */
    public static boolean create(File file, InputStream inputStream) {
        if (isNull(inputStream)) {
            log.error("inputStream is null");
            return false;
        }
        if (!create(file)) {
            return false;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[1024 * 5];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            log.error("Create file failed", e);
            return false;
        } finally {
            if (isNotNull(bis)) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("Close BufferedInputStream failed", e);
                }
            }
            if (isNotNull(bos)) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("Close BufferedOutputStream failed", e);
                }
            }
        }
    }




}
