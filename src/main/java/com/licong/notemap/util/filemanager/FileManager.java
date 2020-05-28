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
     * 重命名
     *
     * @param source  源文件
     * @param newName 新名字
     * @return
     */
    public static boolean rename(File source, String newName) {
        if (isNull(source) || StringUtils.isEmpty(newName)) {
            log.error("source is null or newName is empty");
            return false;
        }
        if (!source.exists()) {
            log.error(source.getAbsolutePath() + " is not exist");
            return false;
        }
        String path = source.getAbsolutePath();
        String name = source.getName();
        int index = name.indexOf('.');
        if (-1 != index) {
            return source.renameTo(new File(path.substring(0, path.indexOf(name)) + File.separator + newName + name.substring(index)));
        }
        return source.renameTo(new File(path.substring(0, path.indexOf(name)) + File.separator + newName));
    }

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param target 目的文件
     * @return 成功返回true，失败返回false
     */
    public static boolean copy(File source, File target) {
        if (isNull(source) || isNull(target)) {
            log.error("source is null or target is null");
            return false;
        }
        if (!source.exists()) {
            log.error(source.getAbsolutePath() + " is not exist");
            return false;
        }
        //改用NIO复制文件
        boolean createSuccess = create(target);
        if (!createSuccess) {
            return false;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 500);
        FileChannel fi = null;
        FileChannel fo = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            fi = fis.getChannel();
            fo = fos.getChannel();
            int r = 0;
            while (r != -1) {
                buffer.clear();
                r = fi.read(buffer);
                buffer.flip();
                fo.write(buffer);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (isNotNull(fi)) {
                try {
                    fi.close();
                } catch (IOException e) {
                    log.error("Close Input FileChannel failed", e);
                }
            }
            if (isNotNull(fo)) {
                try {
                    fo.close();
                } catch (IOException e) {
                    log.error("Close Output FileChannel failed", e);
                }
            }
            if (isNotNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream failed", e);
                }
            }
            if (isNotNull(fos)) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("Close FileOutputStream failed", e);
                }
            }
        }

    }

    /**
     * 剪切文件
     *
     * @param source 源文件
     * @param target 目的文件
     * @return 成功返回true，失败返回false
     */
    public static boolean cut(File source, File target) {
        if (copy(source, target)) {
            delete(source);
            return true;
        }
        return false;
    }

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

    /**
     * 创建文件
     *
     * @param file    待创建文件
     * @param content 文件内容
     * @return 成功返回true，失败返回false
     */
    public static boolean create(File file, byte[] content) {
        if (isNull(file) || isNull(content)) {
            log.error("file is null or content is null");
            return false;
        }
        if (file.exists()) {
            log.error("File is already exist");
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(content);
            bos.flush();
            return true;
        } catch (IOException e) {
            log.error("Create file failed", e);
            return false;
        } finally {
            if (isNotNull(bos)) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("Close BufferedOutputStream failed", e);
                }
            }
        }
    }

    /**
     * 创建文件
     *
     * @param file    待创建文件
     * @param content 文件内容
     * @return 成功返回true，失败返回false
     */
    public static boolean create(File file, String content) {
        return create(file, content.getBytes());
    }


    /**
     * 覆盖文件
     *
     * @param file        目标文件
     * @param inputStream 输入流
     * @return 成功返回true，失败返回false
     */
    public static boolean cover(File file, InputStream inputStream) {
        if (!delete(file)) {
            return false;
        }
        return create(file, inputStream);
    }

    /**
     * 覆盖文件
     *
     * @param file    目标文件
     * @param content 文件内容
     * @return 成功返回true，失败返回false
     */
    public static boolean cover(File file, String content) {
        if (!delete(file)) {
            return false;
        }
        return create(file, content);
    }

    /**
     * * 覆盖文件
     *
     * @param file    目标文件
     * @param content 文件内容
     * @return 成功返回true，失败返回false
     */
    public static boolean cover(File file, byte[] content) {
        if (!delete(file)) {
            return false;
        }
        return create(file, content);
    }

    /**
     * 追加文件
     *
     * @param file   目标
     * @param append 追加内容
     * @return 成功返回true，失败返回false
     */
    public static boolean append(File file, String append) {
        if (isNull(file) || isNull(append)) {
            log.error("file is nul or append is null");
            return false;
        }
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(append.getBytes());
            return true;
        } catch (IOException e) {
            log.error("Append file failed", e);
            return false;
        } finally {
            if (isNotNull(randomFile)) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    log.error("Close RandomAccessFile failed", e);
                }
            }
        }
    }

    /**
     * 追加文件
     *
     * @param file   目标
     * @param append 追加内容
     * @return 成功返回true，失败返回false
     */
    public static boolean append(File file, byte[] append) {
        if (isNull(file) || isNull(append)) {
            log.error("file is nul or append is null");
            return false;
        }
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(append);
            return true;
        } catch (IOException e) {
            log.error("Append file failed", e);
            return false;
        } finally {
            if (isNotNull(randomFile)) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    log.error("Close RandomAccessFile failed", e);
                }
            }
        }
    }

    /**
     * 追加文件
     *
     * @param file        目标
     * @param inputStream 追加输入流
     * @return 成功返回true，失败返回false
     */
    public static boolean append(File file, InputStream inputStream) {
        if (isNull(file) || isNull(inputStream)) {
            log.error("file is nul or inputStream is null");
            return false;
        }
        BufferedInputStream bis = null;
        RandomAccessFile randomFile = null;
        try {
            bis = new BufferedInputStream(inputStream);
            randomFile = new RandomAccessFile(file, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            byte[] buffer = new byte[1024 * 5];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                randomFile.write(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            log.error("Append file failed", e);
            return false;
        } finally {
            if (isNotNull(bis)) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("Close BufferedInputStream failed", e);
                }
            }
            if (isNotNull(randomFile)) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    log.error("Close RandomAccessFile failed", e);
                }
            }
        }
    }

    /**
     * 替换文本内容
     *
     * @param file        目标文件
     * @param target      查找目标
     * @param replaceText 替换为
     * @param result      替换结果，当不关心结果时可以为null
     * @return 成功返回true，失败返回false
     * @see FileReplaceResult
     */
    public static boolean replace(File file, String target, String replaceText, FileReplaceResult result) {
        //TODO 对目录的情况
        if (isNull(file) || StringUtils.isEmpty(target) || isNull(replaceText)) {
            log.error("file is nul or target is empty or replaceText is null");
            return false;
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        //创建临时文件
        File temp = new File(file.getAbsolutePath() + ".tmp");
        create(temp);
        int count = 0;
        if (isNotNull(result)) {
            result.setFile(file);
        }
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(temp));
            String line = null;
            while (StringUtils.isNotEmpty(line = br.readLine())) {
                String newLine = line.replaceAll(target, replaceText);
                if (isNotNull(result) && !newLine.equals(line)) {
                    count++;
                }
                bw.write(newLine + "\n");
            }
            if (isNotNull(result)) {
                result.setCount(count);
            }
        } catch (FileNotFoundException e) {
            log.error("Not found " + file.getAbsolutePath(), e);
            return false;
        } catch (IOException e) {
            log.error("replace " + file.getAbsolutePath() + " failed", e);
            return false;
        } finally {
            if (isNotNull(br)) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("Close BufferedReader failed", e);
                }
            }
            if (isNotNull(bw)) {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.error("Close BufferedReader failed", e);
                }
            }
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(temp);
            if (!cover(file, fis)) {
                return false;
            }
        } catch (FileNotFoundException e) {
            log.error("Not found " + temp.getAbsolutePath(), e);
            return false;
        } finally {
            if (isNotNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream failed", e);
                }
            }
        }
        return delete(temp);
    }

    /**
     * 搜索文本内容
     *
     * @param file   目标文件
     * @param target 查找目标
     * @param result 搜索结果
     * @return 成功返回true，失败(发生异常)返回false
     * @see FileReplaceResult
     */
    public static boolean search(File file, String target, SingleFileSearchResult result) {
        if (isNull(file) || StringUtils.isEmpty(target) || isNull(result) || file.isDirectory()) {
            log.error("file is null or target is empty or result is null or file is Directory!");
            return false;
        }
        BufferedReader br = null;
        //创建临时文件
        int count = 0;
        result.setFile(file);
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while (StringUtils.isNotEmpty(line = br.readLine())) {
                if (-1 != line.indexOf(target)) {
                    result.setLine(line);
                    count++;
                }
            }
            result.setCount(count);
            return true;
        } catch (FileNotFoundException e) {
            log.error("Not found " + file.getAbsolutePath(), e);
            return false;
        } catch (IOException e) {
            log.error("replace " + file.getAbsolutePath() + " failed", e);
            return false;
        } finally {
            if (isNotNull(br)) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("Close BufferedReader failed", e);
                }
            }
        }
    }


    /**
     * 裁剪文件
     *
     * @param file   文件
     * @param length 裁剪长度,单位byte
     */
    public static boolean truncate(File file, long length) {
        if (isNull(file) || file.isDirectory()) {
            log.error("file is null or file is directory");
            return false;
        }
        if (file.length() == length) {
            return true;
        }
        FileChannel fileChannel = null;
        try {
            fileChannel = new RandomAccessFile(file, "rw").getChannel();
            fileChannel.truncate(length);
            fileChannel.close();
            return true;
        } catch (Exception e) {
            log.error("Truncate file " + file.getAbsolutePath() + " failed", e);
            return false;
        } finally {
            if (isNotNull(fileChannel)) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    log.error("Close fileChannel failed", e);
                }
            }
        }
    }


}
