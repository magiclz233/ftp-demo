package com.magic.service;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author magic_lz
 * @version 1.0
 * @classname FtpProperties
 * @date 2021/4/19 : 20:01
 */
public interface FtpProcessor {

    boolean uploadFile(String path, String fileName, String originFileName);

    boolean uploadFile(String path, String fileName, InputStream inputStream);

    boolean downloadFile(String path, String fileName, String localPath);

    boolean deleteFile(String path, String fileName);

    boolean createDirectory(String remote, FTPClient ftpClient) throws IOException;

    boolean existFile(String path, FTPClient ftpClient) throws IOException;

    boolean makeDirectory(String directory, FTPClient ftpClient);

    List<String> retrieveFileNames(String remotePath) throws IOException;
}
