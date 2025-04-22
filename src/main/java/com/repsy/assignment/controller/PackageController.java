package com.repsy.assignment.controller;

import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.dto.PackageDTO;
import com.repsy.assignment.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Package operations
 */
@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    /**
     * Create a new package
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PackageDTO>> createPackage(@RequestParam String name) {
        try {
            PackageDTO pkg = packageService.createPackage(name);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(pkg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get a package by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageDTO>> getPackage(@PathVariable Long id) {
        try {
            PackageDTO pkg = packageService.getPackageById(id);
            return ResponseEntity.ok(ApiResponse.success(pkg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Get a package by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<PackageDTO>> getPackageByName(@PathVariable String name) {
        try {
            PackageDTO pkg = packageService.getPackageByName(name);
            return ResponseEntity.ok(ApiResponse.success(pkg));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * List all packages
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageDTO>>> getAllPackages() {
        List<PackageDTO> packages = packageService.getAllPackages();
        return ResponseEntity.ok(ApiResponse.success(packages));
    }

    /**
     * Delete a package
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable Long id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Check if a package exists
     */
    @GetMapping("/exists/{name}")
    public ResponseEntity<ApiResponse<Boolean>> packageExists(@PathVariable String name) {
        boolean exists = packageService.packageExists(name);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
