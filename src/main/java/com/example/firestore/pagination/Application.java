package com.example.firestore.pagination;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {

    @PostConstruct
    public void initFirebase() throws IOException {
        if(FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream("path/to/firebase-service-account.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}