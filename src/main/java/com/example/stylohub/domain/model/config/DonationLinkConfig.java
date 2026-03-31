package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonationLinkConfig implements WidgetConfig {

    private final DonationPlatform platform;
    private final String url;
    private final String title;

    @JsonCreator
    public DonationLinkConfig(
            @JsonProperty("platform") DonationPlatform platform,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title) {
        this.platform = platform;
        this.url = url;
        this.title = title;
        validate();
    }

    @Override
    public void validate() {
        if (platform == null) throw new DomainValidationException("Plataforma de doação é obrigatória.");
        if (url == null || url.isBlank()) throw new DomainValidationException("URL de doação é obrigatória.");
        if (!url.matches("^https?://\\S+$")) throw new DomainValidationException("URL de doação inválida.");
    }

    @Override
    public WidgetType getType() { return WidgetType.DONATION_LINK; }

    public DonationPlatform getPlatform() { return platform; }
    public String getUrl() { return url; }
    public String getTitle() { return title; }
}
