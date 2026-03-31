package com.example.stylohub.adapter.in.web.dto;

import com.example.stylohub.domain.model.WidgetType;
import com.example.stylohub.domain.model.config.DonationPlatform;
import com.example.stylohub.domain.model.config.PixKeyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record AddWidgetRequest(
        @NotNull WidgetType type,
        @Min(0) int order,
        String title,
        @Pattern(regexp = "^$|^https?://\\S+$", message = "URL deve começar com http:// ou https://")
        String url,
        String videoId,
        Boolean autoPlay,
        Boolean showControls,
        String spotifyUri,
        Boolean compact,
        @Pattern(regexp = "^$|^https?://\\S+$", message = "URL deve começar com http:// ou https://")
        String imageUrl,
        String altText,
        @Pattern(regexp = "^$|^https?://\\S+$", message = "URL deve começar com http:// ou https://")
        String linkUrl,
        String content,
        String buttonLabel,
        String successMessage,
        List<String> formFields,
        // New embed fields
        String twitchChannel,
        String twitchClipSlug,
        String twitterTweetId,
        // New monetization fields
        DonationPlatform donationPlatform,
        String pixKey,
        PixKeyType pixKeyType
) {}
