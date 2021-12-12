package com.smartcity.springapplication.notification

data class Notification(
    val title: String? = null,
    val message: String? = null,
    val type: NotificationType? = null,
    val topic: String? = null,
    val token: String? = null,
)