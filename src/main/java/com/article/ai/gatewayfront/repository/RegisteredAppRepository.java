package com.article.ai.gatewayfront.repository;

import com.article.ai.gatewayfront.entity.RegisteredApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredAppRepository extends JpaRepository<RegisteredApp, Long> {
    Optional<RegisteredApp> findByAppName(String appName);
    List<RegisteredApp> findByActive(boolean active);
    List<RegisteredApp> findByCreatedBy(String createdBy);
}

