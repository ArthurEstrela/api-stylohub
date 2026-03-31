package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundCloudConfig implements WidgetConfig {

    private final String trackUrl;
    private final boolean compact;

    @JsonCreator
    public SoundCloudConfig(
            @JsonProperty("trackUrl") String trackUrl,
            @JsonProperty("compact") boolean compact) {
        this.trackUrl = trackUrl;
        this.compact = compact;
        this.validate();
    }

    public String getTrackUrl() { return trackUrl; }
    public boolean isCompact() { return compact; }

    @Override
    public WidgetType getType() { return WidgetType.SOUNDCLOUD; }

    @Override
    public void validate() {
        if (trackUrl == null || trackUrl.isBlank()) {
            throw new DomainValidationException("A URL do SoundCloud não pode estar vazia.");
        }
        if (!trackUrl.startsWith("https://soundcloud.com/")) {
            throw new DomainValidationException("URL do SoundCloud inválida. Deve começar com https://soundcloud.com/");
        }
    }
}
