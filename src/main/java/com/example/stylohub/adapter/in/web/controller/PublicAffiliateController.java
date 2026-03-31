package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.application.port.in.ResolveAffiliateUseCase;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/affiliate")
@Tag(name = "Affiliate Links", description = "Resolução pública de links de afiliado")
public class PublicAffiliateController {

    private final ResolveAffiliateUseCase resolveAffiliateUseCase;
    private final TrackAnalyticsUseCase analyticsUseCase;

    public PublicAffiliateController(ResolveAffiliateUseCase resolveAffiliateUseCase,
                                     TrackAnalyticsUseCase analyticsUseCase) {
        this.resolveAffiliateUseCase = resolveAffiliateUseCase;
        this.analyticsUseCase = analyticsUseCase;
    }

    @GetMapping("/{code}")
    @Operation(summary = "Resolve um link de afiliado pelo código curto e registra o clique")
    public ResponseEntity<Map<String, String>> resolveAffiliate(@PathVariable String code) {
        ResolveAffiliateUseCase.AffiliateResult result = resolveAffiliateUseCase.resolve(code);
        analyticsUseCase.recordWidgetClick(result.profileId(), result.widgetId());
        return ResponseEntity.ok(Map.of("url", result.url()));
    }
}
