package io.featurehub.examples.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {

        // Start specmatic.jar in a separate thread
        new Thread(() -> {
            try {
                Process process = new ProcessBuilder("java", "-jar", "specmatic.jar", "stub").start();
                process.waitFor(); // Wait for the process to complete (optional)
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Start Spring Boot application
        SpringApplication.run(MyApplication.class, args);
    }
}
