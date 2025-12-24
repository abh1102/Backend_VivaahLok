package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

@Data
public class UpdateSettingsRequest {
    private Boolean notifications;
    private String language;
}
