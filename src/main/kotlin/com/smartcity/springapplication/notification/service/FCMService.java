package com.smartcity.springapplication.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartcity.springapplication.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {
    public void sendMessageCustomDataWithTopic(Map<String, String> data, Notification notification)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithDataCustomWithTopic(data, notification);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        //log.info("Sent message with data. Topic: " + data.get("topic") + ", " + "Type "+ data.get("type")+", "+ response+ " msg "+jsonOutput);
    }

    private Message getPreconfiguredMessageWithDataCustomWithTopic(Map<String, String> data, Notification notification) {
        return getPreconfiguredMessageBuilderCustomDataWithTopic(data).putAllData(data).setTopic(notification.getTopic())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilderCustomDataWithTopic(Map<String, String> data) {
        return Message.builder()
                .putAllData(data);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }
}
