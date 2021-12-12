package com.smartcity.springapplication.notification.configuration

import com.google.auth.oauth2.GoogleCredentials
import org.springframework.core.io.ClassPathResource
import com.google.firebase.FirebaseOptions
import com.google.firebase.FirebaseApp
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct

/*@Service
class FCMInitializer {
    @Value("\${app.firebase-configuration-file}")
    private val firebaseConfigPath: String? = null
    @PostConstruct
    fun initialize() {
        try {
            val googleCredentials = GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath!!).inputStream)
            val options = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build()
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                //FCMInitializer.log.info("Firebase application has been initialized")
            }
        } catch (e: IOException) {
            //FCMInitializer.log.error(e.message)
        }
    }
}*/