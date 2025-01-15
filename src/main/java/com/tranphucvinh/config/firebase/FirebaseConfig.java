package com.tranphucvinh.config.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tranphucvinh.config.util.Utils;

//@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        initFirebaseApp();
    }

    @Value("${firebase.adminsdk.dir}")
    private String firebaseAdminsdkJsonDir;

    private GoogleCredentials googleCredentials() {
        try {

            InputStream inputStream = Utils.readResourceFile(firebaseAdminsdkJsonDir);

            if (Objects.nonNull(inputStream)) {
                return GoogleCredentials.fromStream(inputStream);
            }
            else {
                // Use standard credentials chain. Useful when running inside GKE
                return GoogleCredentials.getApplicationDefault();
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void initFirebaseApp() {
        FirebaseOptions serviceOptions = FirebaseOptions.builder().setCredentials(this.googleCredentials())
                .build();
        FirebaseApp.getApps().stream().filter(firebaseApp -> firebaseApp.getName().equals("engMindMapFirebase"))
                .findFirst().orElseGet(() -> FirebaseApp.initializeApp(serviceOptions, "engMindMapFirebase"));
    }

    @Bean(name = "engMindMapFirebase")
    FirebaseMessaging engMindMapFirebaseMessage() {
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance("engMindMapFirebase"));
    }
}
