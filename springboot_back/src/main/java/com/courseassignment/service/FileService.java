package com.courseassignment.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传文件
     * @return 文件存储路径
     */
    String upload(MultipartFile file);

    /**
     * 下载文件
     */
    Resource download(String filePath);
}
