package com.lectorie.lectorie.dto.request;

import jakarta.annotation.Nullable;


public record ChangeSettingsRequest(
        @Nullable
        ChangeUserSettingsRequest changeUserSettingsRequest,

        @Nullable
        ChangeTutorSettingsRequest changeTutorSettingsRequest,

        @Nullable
        ChangeUserRequest changeUserRequest
) {
}
