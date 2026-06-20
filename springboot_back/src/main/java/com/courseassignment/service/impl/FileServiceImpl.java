package com.courseassignment.service.impl;

import com.courseassignment.config.FileUploadProperties;
import com.courseassignment.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileUploadProperties fileUploadProperties;

    public FileServiceImpl(FileUploadProperties fileUploadProperties) {
        this.fileUploadProperties = fileUploadProperties;
    }

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        try {
            String uploadPath = fileUploadProperties.getPath();
            // 确保上传目录存在
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID().toString() + extension;

            // 按日期分目录存储
            String dateDir = java.time.LocalDate.now().toString().replace("-", "/");
            File targetDir = new File(uploadDir, dateDir);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // 保存文件
            Path targetPath = Paths.get(targetDir.getAbsolutePath(), storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return dateDir + "/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public Resource download(String filePath) {
        try {
            String uploadPath = fileUploadProperties.getPath();
            Path file = Paths.get(uploadPath).resolve(filePath).normalize();
            URI uri = Objects.requireNonNull(file.toUri());
            Resource resource = UrlResource.from(uri);

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("文件不存在或无法读取");
            }
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }
}
