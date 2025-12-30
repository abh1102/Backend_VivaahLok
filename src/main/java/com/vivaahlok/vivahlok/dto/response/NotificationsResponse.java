package com.vivaahlok.vivahlok.dto.response;

import com.vivaahlok.vivahlok.dto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsResponse {
    private List<NotificationDTO> notifications;
    private long unreadCount;
    private long total;
    private int page;
    private int pages;
}
