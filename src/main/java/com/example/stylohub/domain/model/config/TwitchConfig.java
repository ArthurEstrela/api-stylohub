package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchConfig implements WidgetConfig {

    private final String channel;
    private final String clipSlug;
    private final boolean isClip;

    @JsonCreator
    public TwitchConfig(
            @JsonProperty("channel") String channel,
            @JsonProperty("clipSlug") String clipSlug,
            @JsonProperty("isClip") boolean isClip) {
        this.channel = channel;
        this.clipSlug = clipSlug;
        this.isClip = isClip;
        this.validate();
    }

    public String getChannel() { return channel; }
    public String getClipSlug() { return clipSlug; }
    @JsonProperty("isClip")
    public boolean isClip() { return isClip; }

    @Override
    public WidgetType getType() { return WidgetType.TWITCH; }

    @Override
    public void validate() {
        if (isClip) {
            if (clipSlug == null || clipSlug.isBlank()) {
                throw new DomainValidationException("O slug do clip do Twitch não pode estar vazio.");
            }
            if (!clipSlug.matches("[a-zA-Z0-9_\\-]+")) {
                throw new DomainValidationException("Slug do clip do Twitch inválido (apenas letras, números, _ e -).");
            }
        } else {
            if (channel == null || channel.isBlank()) {
                throw new DomainValidationException("O nome do canal do Twitch não pode estar vazio.");
            }
            if (!channel.matches("[a-zA-Z0-9_]{1,25}")) {
                throw new DomainValidationException("Nome de canal Twitch inválido (apenas letras, números e _, máx 25 chars).");
            }
        }
    }
}
