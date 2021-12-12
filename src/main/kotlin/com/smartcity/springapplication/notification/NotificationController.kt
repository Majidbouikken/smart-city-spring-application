package com.smartcity.springapplication.notification

import com.smartcity.springapplication.notification.service.NotificationService
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notification")
class NotificationController @Autowired constructor(
    val notificationService: NotificationService
) {
    @PostMapping("/topic")
    @ResponseStatus(value = HttpStatus.OK)
    fun sendNotification(@RequestBody notification: Notification?): Response<String> {
        notificationService.sendNotification(notification)
        return Response("Notification has been sent.")
    }

    /* @PostMapping("/token")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> sendTokenNotification(@RequestBody Notification request) {
        fcmNotificationService.sendNotificationToToken(request);
        return new Response<>("Notification has been sent.");
    }*/
}