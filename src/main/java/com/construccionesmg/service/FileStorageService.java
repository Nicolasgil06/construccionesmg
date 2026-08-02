package com.construccionesmg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService() {
        this.uploadDir = Paths.get("uploads/cotizaciones").toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida: " + uploadDir, e);
        }
    }

    public String storePdf(MultipartFile file, String cotizacionId) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF");
        }

        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = "cotizacion-" + cotizacionId + "-" + UUID.randomUUID() + extension;
        Path target = uploadDir.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo PDF", e);
        }
    }

    public Path getUploadDir() {
        return uploadDir;
    }
}
