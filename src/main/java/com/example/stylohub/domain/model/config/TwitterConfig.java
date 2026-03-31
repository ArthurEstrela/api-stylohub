package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterConfig implements WidgetConfig {

    private final String tweetId;

    @JsonCreator
    public TwitterConfig(@JsonProperty("tweetId") String tweetId) {
        this.tweetId = tweetId;
        this.validate();
    }

    public String getTweetId() { return tweetId; }

    @Override
    public WidgetType getType() { return WidgetType.TWITTER; }

    @Override
    public void validate() {
        if (tweetId == null || tweetId.isBlank()) {
            throw new DomainValidationException("O ID do tweet não pode estar vazio.");
        }
        if (!tweetId.matches("\\d+")) {
            throw new DomainValidationException("O ID do tweet deve ser numérico.");
        }
    }
}
