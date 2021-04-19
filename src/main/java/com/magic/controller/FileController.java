package com.magic.controller;

import com.magic.service.FtpProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author magic_lz
 * @version 1.0
 * @classname FileController
 * @date 2021/4/19 : 19:55
 */
@RestController
public class FileController {

    private final FtpProcessor ftpProcessor;

    public FileController(FtpProcessor ftpProcessor) {
        this.ftpProcessor = ftpProcessor;
    }

    /**
     * 上传文件到ftp服务器
     *
     * @param file 上传的文件
     * @return 上传结果
     */
    @PostMapping("/uploadFtp")
    public String uploadFtp(MultipartFile file) throws IOException {
        String url = "F:/ftp/test";
        InputStream in = new ByteArrayInputStream(file.getBytes());
        boolean b = ftpProcessor.uploadFile(url, "test.txt", in);
        if (b) {
            return "success";
        } else {
            return "fail";
        }
    }


    @GetMapping("/downloadFile")
    public String downloadFile() {
        boolean b = ftpProcessor.downloadFile("F:\\ftp", "test.txt", "F:\\test");
        if (b) {
            return "success";
        } else {
            return "fail";
        }
    }
}
