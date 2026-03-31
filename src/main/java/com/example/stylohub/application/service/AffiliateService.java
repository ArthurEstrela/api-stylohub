package com.example.stylohub.application.service;

import com.example.stylohub.application.port.in.ResolveAffiliateUseCase;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AffiliateService implements ResolveAffiliateUseCase {

    private final ProfileRepositoryPort profileRepo;

    public AffiliateService(ProfileRepositoryPort profileRepo) {
        this.profileRepo = profileRepo;
    }

    @Override
    public AffiliateResult resolve(String code) {
        ProfileRepositoryPort.AffiliateWidgetInfo info = profileRepo.findByAffiliateCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Link de afiliado", code));
        return new AffiliateResult(info.url(), info.profileId(), info.widgetId());
    }
}
