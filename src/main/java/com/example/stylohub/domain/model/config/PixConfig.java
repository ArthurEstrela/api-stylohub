package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PixConfig implements WidgetConfig {

    private final String pixKey;
    private final PixKeyType pixKeyType;
    private final String title;
    private final String description;

    @JsonCreator
    public PixConfig(
            @JsonProperty("pixKey") String pixKey,
            @JsonProperty("pixKeyType") PixKeyType pixKeyType,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description) {
        this.pixKey = pixKey;
        this.pixKeyType = pixKeyType;
        this.title = title;
        this.description = description;
        validate();
    }

    @Override
    public void validate() {
        if (pixKey == null || pixKey.isBlank()) throw new DomainValidationException("Chave PIX é obrigatória.");
        if (pixKeyType == null) throw new DomainValidationException("Tipo de chave PIX é obrigatório.");
    }

    @Override
    public WidgetType getType() { return WidgetType.PIX; }

    public String getPixKey() { return pixKey; }
    public PixKeyType getPixKeyType() { return pixKeyType; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
