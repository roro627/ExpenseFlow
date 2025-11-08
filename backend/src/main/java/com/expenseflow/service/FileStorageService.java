package com.expenseflow.service;

import com.expenseflow.config.FileStorageProperties;
import com.expenseflow.exception.BadRequestException;
import com.expenseflow.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "pdf");
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024;

    private final Path root;

    public FileStorageService(FileStorageProperties properties) {
        this.root = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() throws IOException {
        Files.createDirectories(root);
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new BadRequestException("File exceeds 5MB limit");
        }
        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException("Only png, jpg, jpeg, pdf are allowed");
        }

        String normalized = StringUtils.cleanPath(file.getOriginalFilename());
        if (normalized.contains("..")) {
            throw new BadRequestException("Invalid file name");
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String filename = timestamp + "-" + normalized.replaceAll("\\s+", "_");
        Path destination = root.resolve(filename);
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file");
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename).normalize();
            if (!file.startsWith(root)) {
                throw new BadRequestException("Invalid file path");
            }
            if (!Files.exists(file)) {
                throw new NotFoundException("File not found");
            }
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (IOException ex) {
            throw new NotFoundException("File not accessible");
        }
        throw new NotFoundException("File not accessible");
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
