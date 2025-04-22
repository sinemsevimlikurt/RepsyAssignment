package com.repsy.assignment.controller;

import com.repsy.assignment.dto.ApiResponse;
import com.repsy.assignment.dto.DependencyDTO;
import com.repsy.assignment.service.DependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Dependency operations
 */
@RestController
@RequestMapping("/api/v1/versions/{versionId}/dependencies")
@RequiredArgsConstructor
public class DependencyController {

    private final DependencyService dependencyService;

    /**
     * Add a new dependency
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DependencyDTO>> addDependency(
            @PathVariable Long versionId,
            @RequestParam String packageName,
            @RequestParam String version) {
        try {
            DependencyDTO dependency = dependencyService.addDependency(versionId, packageName, version);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(dependency));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * List dependencies of a version
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DependencyDTO>>> getDependencies(@PathVariable Long versionId) {
        List<DependencyDTO> dependencies = dependencyService.getDependencies(versionId);
        return ResponseEntity.ok(ApiResponse.success(dependencies));
    }

    /**
     * Find versions depending on a package
     */
    @GetMapping("/dependents/{packageName}")
    public ResponseEntity<ApiResponse<List<DependencyDTO>>> findDependents(
            @PathVariable String packageName) {
        List<DependencyDTO> dependents = dependencyService.findDependents(packageName);
        return ResponseEntity.ok(ApiResponse.success(dependents));
    }

    /**
     * Remove a dependency
     */
    @DeleteMapping("/{dependencyId}")
    public ResponseEntity<ApiResponse<Void>> removeDependency(
            @PathVariable Long versionId,
            @PathVariable Long dependencyId) {
        try {
            dependencyService.removeDependency(dependencyId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    /**
     * Check if dependency exists
     */
    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> dependencyExists(
            @PathVariable Long versionId,
            @RequestParam String packageName,
            @RequestParam String version) {
        boolean exists = dependencyService.dependencyExists(versionId, packageName, version);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Check for circular dependencies
     */
    @GetMapping("/check-circular")
    public ResponseEntity<ApiResponse<Boolean>> checkCircularDependency(
            @PathVariable Long versionId,
            @RequestParam String packageName) {
        boolean hasCircular = dependencyService.hasCircularDependency(versionId, packageName);
        return ResponseEntity.ok(ApiResponse.success(hasCircular));
    }
}
