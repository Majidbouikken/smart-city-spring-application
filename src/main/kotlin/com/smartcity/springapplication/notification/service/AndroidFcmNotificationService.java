package com.smartcity.springapplication.notification.service;

import com.smartcity.springapplication.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AndroidFcmNotificationService implements NotificationService {
    private final FCMService fcmService;

    public AndroidFcmNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    public void sendNotification(Notification notification) {
        try {
            fcmService.sendMessageCustomDataWithTopic(getSamplePayloadDataCustom(notification), notification);
        } catch (Exception e) {
            //log.error(e.getMessage());
        }
    }

    private Map<String, String> getSamplePayloadDataCustom(Notification notification) {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("title", notification.getTitle());
        pushData.put("type",notification.getType().toString());
        pushData.put("message", notification.getMessage());
        return pushData;
    }
}