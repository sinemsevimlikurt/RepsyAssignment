package com.repsy.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Model class for package metadata (meta.json)
 */
@Data
public class PackageMeta {
    private String name;
    private String version;
    private String author;
    private List<PackageMetaDependency> dependencies;
    
    @Data
    public static class PackageMetaDependency {
        @JsonProperty("package")
        private String packageName;
        private String version;
    }
}
