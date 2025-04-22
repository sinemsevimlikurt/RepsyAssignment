package com.repsy.assignment.controller;

import com.repsy.assignment.storage.StorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * Controller for package download operations
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DownloadController {

    private final StorageStrategy storageStrategy;

    /**
     * Download a package file
     */
    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) {
        
        try {
            // Check if file exists
            if (!storageStrategy.exists(packageName, version, fileName)) {
                return ResponseEntity.notFound().build();
            }

            // Get file content
            InputStream fileContent = storageStrategy.retrieve(packageName, version, fileName);
            InputStreamResource resource = new InputStreamResource(fileContent);

            // Set content type based on file extension
            MediaType contentType = fileName.endsWith(".json") 
                    ? MediaType.APPLICATION_JSON 
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
