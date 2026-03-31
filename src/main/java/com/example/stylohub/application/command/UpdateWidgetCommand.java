package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.config.DonationPlatform;
import com.example.stylohub.domain.model.config.PixKeyType;

import java.util.List;

public record UpdateWidgetCommand(
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
        String twitterTweetId,
        // New monetization fields
        DonationPlatform donationPlatform,
        String pixKey,
        PixKeyType pixKeyType
) {}
