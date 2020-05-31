package com.licong.notemap.web.controller;

import com.licong.notemap.util.StringUtils;
import com.licong.notemap.util.filemanager.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class ImageController {

    private static final String IMAGE_PARENT_URI = "/images/";
    private static final String LINUX = "linux";

    @Value("${image.storage}")
    private String imageStorage;

    @Value("${image.system}")
    private String imageSystem;


    @RequestMapping("/imageUpload")
    @ResponseBody
    public Map<String, Object> saveImage(@RequestParam("editormd-image-file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        FileManager fileManager = new FileManager();
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            result.put("success", 0);//图片上传失败的信息码
            result.put("message", "upload error!");//信息
            return result;
        }
        File source = new File(imageStorage + fileName);
        try {
            fileManager.create(source, file.getInputStream());

            if (LINUX.equals(imageSystem)) {
                //linux 文件授权
                String command = "chmod 775 " + imageStorage + fileName;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(command);
            }

            result.put("url", IMAGE_PARENT_URI + fileName);//图片回显地址，即文件存放地址，应为虚拟路径
            result.put("success", 1);//图片上传成功的信息码
            result.put("message", "upload success!");//信息
        } catch (IOException e) {
            log.error("upload error", e);
            result.put("success", 0);//图片上传失败的信息码
            result.put("message", "upload error!");//信息
            return result;
        }
        return result;
    }
}
