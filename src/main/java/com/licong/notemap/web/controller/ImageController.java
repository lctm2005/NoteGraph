package com.licong.notemap.web.controller;

import com.licong.notemap.util.filemanager.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class ImageController {

    @RequestMapping("/imageUpload")
    @ResponseBody
    public Map<String, Object> saveImage(@RequestParam("editormd-image-file") MultipartFile file) {
        FileManager fileManager = new FileManager();
        String fileName = UUID.randomUUID() + ".png";
        File source = new File("/usr/local/static/picture/" + fileName);
        Map<String, Object> result = new HashMap<>();
        try {
            fileManager.create(source, file.getInputStream());
            result.put("url", "/picture/" + fileName);//图片回显地址，即文件存放地址，应为虚拟路径
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
