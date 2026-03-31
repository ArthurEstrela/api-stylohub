package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AffiliateLinkConfig implements WidgetConfig {

    private final String url;
    private final String code;
    private final String title;

    @JsonCreator
    public AffiliateLinkConfig(
            @JsonProperty("url") String url,
            @JsonProperty("code") String code,
            @JsonProperty("title") String title) {
        this.url = url;
        this.code = code;
        this.title = title;
        validate();
    }

    @Override
    public void validate() {
        if (url == null || url.isBlank()) throw new DomainValidationException("URL do afiliado é obrigatória.");
        if (!url.matches("^https?://\\S+$")) throw new DomainValidationException("URL do afiliado inválida.");
        if (code == null || code.isBlank()) throw new DomainValidationException("Código do afiliado é obrigatório.");
        if (title == null || title.isBlank()) throw new DomainValidationException("Título do link de afiliado é obrigatório.");
    }

    @Override
    public WidgetType getType() { return WidgetType.AFFILIATE_LINK; }

    public String getUrl() { return url; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
}
