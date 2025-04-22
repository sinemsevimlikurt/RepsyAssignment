package com.repsy.assignment.service;

import com.repsy.assignment.dto.PackageFileDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Service interface for PackageFile operations
 */
public interface PackageFileService {
    
    /**
     * Upload a new file
     * @param versionId Version ID
     * @param file File to upload
     * @param strategy Storage strategy
     * @return Created file metadata
     */
    PackageFileDTO uploadFile(Long versionId, MultipartFile file, String strategy);
    
    /**
     * Get file metadata
     * @param id File ID
     * @return File metadata if found
     */
    PackageFileDTO getFile(Long id);
    
    /**
     * List all files in a version
     * @param versionId Version ID
     * @return List of files
     */
    List<PackageFileDTO> getFiles(Long versionId);
    
    /**
     * Download a file
     * @param id File ID
     * @return File content as byte array
     */
    byte[] downloadFile(Long id);
    
    /**
     * Delete a file
     * @param id File ID
     */
    void deleteFile(Long id);
    
    /**
     * Check if file exists
     * @param versionId Version ID
     * @param fileName File name
     * @return true if exists
     */
    boolean fileExists(Long versionId, String fileName);
}
