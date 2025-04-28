package com.repsy.repsy_repo_api.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.repsy.repsy_repo_api.Service.PackageService;

@RestController
@RequestMapping("/")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("meta") MultipartFile metaFile,
            @RequestParam("package") MultipartFile packageFile
    ) {
        try {
            packageService.upload(packageName, version, metaFile, packageFile);
            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("{packageName}/{version}/{fileName}")
    public ResponseEntity<byte[]> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) {
        try {
            byte[] fileData = packageService.download(packageName, version, fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}