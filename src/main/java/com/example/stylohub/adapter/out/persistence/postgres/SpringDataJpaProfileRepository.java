package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.ProfileEntity;
import com.example.stylohub.adapter.out.persistence.postgres.entity.WidgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJpaProfileRepository extends JpaRepository<ProfileEntity, UUID> {

    Optional<ProfileEntity> findByUsername(String username);

    Optional<ProfileEntity> findByUserId(UUID userId);

    boolean existsByUsername(String username);

    /**
     * Finds an AFFILIATE_LINK widget by its short code.
     * codeJson param must be: %"code":"<value>"%
     * JOIN FETCH profile to avoid lazy load.
     */
    @Query("SELECT w FROM WidgetEntity w JOIN FETCH w.profile WHERE w.widgetType = 'AFFILIATE_LINK' AND w.configJson LIKE :codeJson ESCAPE '\\\\'")
    Optional<WidgetEntity> findByAffiliateCode(@Param("codeJson") String codeJson);

    @Query("SELECT p FROM ProfileEntity p LEFT JOIN FETCH p.widgets WHERE p.id = :id")
    Optional<ProfileEntity> findByIdWithWidgets(UUID id);
}
