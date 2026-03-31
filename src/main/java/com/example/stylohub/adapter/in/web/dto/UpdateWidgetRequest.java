package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UpdateWidgetRequest(
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
        String twitterTweetId
) {}
