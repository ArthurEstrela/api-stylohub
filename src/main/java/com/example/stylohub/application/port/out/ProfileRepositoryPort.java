package com.example.stylohub.application.port.out;

import com.example.stylohub.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepositoryPort {
    Profile save(Profile profile);
    Optional<Profile> findById(UUID id);
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByUserId(UUID userId);
    boolean existsByUsername(String username);
    void deleteById(UUID id);

    /**
     * Finds a widget of type AFFILIATE_LINK by its short code,
     * returning enough info to record analytics and return the URL.
     */
    record AffiliateWidgetInfo(java.util.UUID widgetId, java.util.UUID profileId, String url) {}
    java.util.Optional<AffiliateWidgetInfo> findByAffiliateCode(String code);
}
