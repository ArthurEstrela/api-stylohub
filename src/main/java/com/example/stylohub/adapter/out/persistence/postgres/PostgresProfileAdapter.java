package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.mapper.JpaProfileMapper;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.model.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresProfileAdapter implements ProfileRepositoryPort {

    private final SpringDataJpaProfileRepository jpaRepo;
    private final JpaProfileMapper mapper;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public PostgresProfileAdapter(SpringDataJpaProfileRepository jpaRepo,
                                   JpaProfileMapper mapper,
                                   com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public Profile save(Profile profile) {
        var entity = mapper.toEntity(profile);
        var saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Profile> findById(UUID id) {
        return jpaRepo.findByIdWithWidgets(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByUserId(UUID userId) {
        return jpaRepo.findByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepo.existsByUsername(username);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public java.util.Optional<ProfileRepositoryPort.AffiliateWidgetInfo> findByAffiliateCode(String code) {
        String escaped = code.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        String codeJson = String.format("%%\"code\":\"%s\"%%", escaped);
        return jpaRepo.findByAffiliateCode(codeJson).map(entity -> {
            try {
                com.example.stylohub.domain.model.config.AffiliateLinkConfig config =
                    objectMapper.readValue(entity.getConfigJson(),
                        com.example.stylohub.domain.model.config.AffiliateLinkConfig.class);
                return new ProfileRepositoryPort.AffiliateWidgetInfo(
                    entity.getId(),
                    entity.getProfile().getId(),
                    config.getUrl()
                );
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new IllegalStateException("Falha ao deserializar AffiliateLinkConfig", e);
            }
        });
    }
}
