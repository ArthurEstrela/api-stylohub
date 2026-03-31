package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TikTokConfig implements WidgetConfig {

    private final String videoId;

    @JsonCreator
    public TikTokConfig(@JsonProperty("videoId") String videoId) {
        this.videoId = videoId;
        this.validate();
    }

    public String getVideoId() { return videoId; }

    @Override
    public WidgetType getType() { return WidgetType.TIKTOK; }

    @Override
    public void validate() {
        if (videoId == null || videoId.isBlank()) {
            throw new DomainValidationException("O ID do vídeo do TikTok não pode estar vazio.");
        }
        if (!videoId.matches("\\d+")) {
            throw new DomainValidationException("O ID do vídeo do TikTok deve ser numérico.");
        }
    }
}
