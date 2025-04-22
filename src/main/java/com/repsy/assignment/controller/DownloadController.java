package com.repsy.assignment.controller;

import com.repsy.assignment.storage.StorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<Resource> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) {
        
        try {
            // Validate file name
            if (!fileName.equals("package.rep") && !fileName.equals("meta.json")) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if file exists
            if (!storageStrategy.exists(packageName, version, fileName)) {
                return ResponseEntity.notFound().build();
            }
            
            // Get file content
            byte[] content = storageStrategy.retrieve(packageName, version, fileName);
            ByteArrayResource resource = new ByteArrayResource(content);
            
            // Set content type
            MediaType contentType = fileName.equals("meta.json") 
                ? MediaType.APPLICATION_JSON 
                : MediaType.APPLICATION_OCTET_STREAM;
            
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
