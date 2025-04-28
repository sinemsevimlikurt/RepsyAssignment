package com.repsy.repsy_repo_api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.repsy.repsy_repo_api.Domain.PackageMetadata;

public interface PackageMetadataRepository extends JpaRepository<PackageMetadata, Long> {
}