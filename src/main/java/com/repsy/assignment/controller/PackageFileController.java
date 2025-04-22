package com.repsy.assignment.controller;

import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.dto.PackageFileDTO;
import com.repsy.assignment.service.PackageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for PackageFile operations
 */
@RestController
@RequestMapping("/api/v1/versions/{versionId}/files")
@RequiredArgsConstructor
public class PackageFileController {

    private final PackageFileService fileService;

    /**
     * Upload a new file
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PackageFileDTO>> uploadFile(
            @PathVariable Long versionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "local") String strategy) {
        try {
            PackageFileDTO fileDTO = fileService.uploadFile(versionId, file, strategy);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(fileDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get file metadata
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<ApiResponse<PackageFileDTO>> getFile(
            @PathVariable Long versionId,
            @PathVariable Long fileId) {
        try {
            PackageFileDTO fileDTO = fileService.getFile(fileId);
            return ResponseEntity.ok(ApiResponse.success(fileDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * List all files in a version
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageFileDTO>>> getFiles(@PathVariable Long versionId) {
        List<PackageFileDTO> files = fileService.getFiles(versionId);
        return ResponseEntity.ok(ApiResponse.success(files));
    }

    /**
     * Download a file
     */
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long versionId,
            @PathVariable Long fileId) {
        try {
            PackageFileDTO fileDTO = fileService.getFile(fileId);
            byte[] content = fileService.downloadFile(fileId);
            
            ByteArrayResource resource = new ByteArrayResource(content);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileDTO.getFileSize())
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Delete a file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable Long versionId,
            @PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Check if file exists
     */
    @GetMapping("/exists/{fileName}")
    public ResponseEntity<ApiResponse<Boolean>> fileExists(
            @PathVariable Long versionId,
            @PathVariable String fileName) {
        boolean exists = fileService.fileExists(versionId, fileName);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
