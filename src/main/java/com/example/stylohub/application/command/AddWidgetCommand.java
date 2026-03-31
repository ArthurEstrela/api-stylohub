package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.WidgetType;

import java.util.List;

public record AddWidgetCommand(
        WidgetType type,
        int order,
        String title,
        String url,
        String videoId,
        Boolean autoPlay,
        Boolean showControls,
        String spotifyUri,
        Boolean compact,
        String imageUrl,
        String altText,
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
