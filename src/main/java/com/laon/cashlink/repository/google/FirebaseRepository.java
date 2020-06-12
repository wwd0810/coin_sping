package com.laon.cashlink.repository.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Slf4j
@Repository
public class FirebaseRepository {

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("cashlink-firebase-adminsdk.json").getInputStream()))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application bas been initialized");
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void send(String token, String title, String body) throws FirebaseMessagingException {
        Notification notification = new Notification(title, body);

        Map<String, String> payload = new HashMap<>();

        {
            payload.put("title", title);
            payload.put("body", body);
        }

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                //TODO: 만약 핸드폰에서 title, body를 못받으면 putData로 title, body를 넣고 테스트
                .putAllData(payload)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info("response: {}", response);
    }

}