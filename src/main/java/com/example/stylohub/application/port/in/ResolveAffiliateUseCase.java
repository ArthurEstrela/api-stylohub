package com.example.stylohub.application.port.in;

import java.util.UUID;

public interface ResolveAffiliateUseCase {

    record AffiliateResult(String url, UUID profileId, UUID widgetId) {}

    AffiliateResult resolve(String code);
}
