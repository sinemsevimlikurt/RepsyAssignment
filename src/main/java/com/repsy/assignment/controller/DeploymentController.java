package com.repsy.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.model.Package;
import com.repsy.assignment.model.PackageMeta;
import com.repsy.assignment.model.PackageVersion;
import com.repsy.assignment.service.PackageService;
import com.repsy.assignment.service.PackageVersionService;
import com.repsy.assignment.storage.StorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for package deployment operations
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeploymentController {

    private final StorageStrategy storageStrategy;
    private final PackageService packageService;
    private final PackageVersionService versionService;
    private final ObjectMapper objectMapper;

    /**
     * Deploy a package file (package.rep or meta.json)
     */
    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<ApiResponse<String>> deployPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validate file name
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.equals("package.rep") && !fileName.equals("meta.json"))) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid file name. Must be 'package.rep' or 'meta.json'"));
            }
            
            // If it's meta.json, validate its content
            if (fileName.equals("meta.json")) {
                PackageMeta meta = objectMapper.readValue(file.getBytes(), PackageMeta.class);
                
                // Validate metadata matches URL parameters
                if (!meta.getName().equals(packageName) || !meta.getVersion().equals(version)) {
                    return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Package name and version in meta.json must match URL parameters"));
                }
                
                // Create or update package and version in database
                if (!packageService.packageExists(packageName)) {
                    packageService.createPackage(packageName);
                }
                
                if (!versionService.versionExists(packageService.getPackageByName(packageName).getId(), version)) {
                    versionService.createVersion(
                        packageService.getPackageByName(packageName).getId(),
                        version,
                        meta.getAuthor()
                    );
                }
            }
            
            // Validate package.rep extension
            if (fileName.equals("package.rep") && !file.getOriginalFilename().endsWith(".rep")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Package file must have .rep extension"));
            }
            
            // Store the file
            String storagePath = storageStrategy.store(packageName, version, fileName, file);
            
            return ResponseEntity.ok(ApiResponse.success(storagePath));
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to process file: " + e.getMessage()));
        }
    }
}
