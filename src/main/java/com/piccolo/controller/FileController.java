package com.piccolo.controller;

import com.piccolo.common.BusinessException;
import com.piccolo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileController {

    @Value("${piccolo.upload.path}")
    private String uploadPath;

    @Value("${piccolo.upload.allowed-types}")
    private String allowedTypes;

    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                : "";
        if (!allowedTypes.contains(extension)) {
            throw new BusinessException("不支持的文件格式，仅支持: " + allowedTypes);
        }

        // 生成唯一文件名
        String newFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        try {
            Path dir = Paths.get(uploadPath).toAbsolutePath();
            Files.createDirectories(dir);
            Path targetPath = dir.resolve(newFilename);
            file.transferTo(targetPath.toFile());
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }

        // 返回可访问的URL
        return Result.success("上传成功~", "/uploads/" + newFilename);
    }
}
