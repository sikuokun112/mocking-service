package io.featurehub.examples.springboot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.featurehub.examples.springboot.service.GitLabService;
import io.featurehub.examples.springboot.service.GitService;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class OpenApiController {

    @Autowired
    private OpenApiResource openApiResource;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GitLabService gitLabService;

    @Autowired
    private GitService gitService;

    @GetMapping("/ping")
    public String ping() {
        System.out.println("PING");
        return "pong";
    }

    @PostMapping("/webhook/gitlab")
    public ResponseEntity<String> handleGitLabWebhook(
            @RequestHeader("X-Gitlab-Event") String event,
            @RequestBody JsonNode payload) {


        // Handle the event based on the type
        if ("Push Hook".equals(event)) {
            System.out.println("Push Hook event received " + event);
            System.out.println("Payload: " + payload);
        }

        return ResponseEntity.ok("Event received");
    }

    @PostMapping("/**")
    public ResponseEntity<String> handleAnyPath(HttpServletRequest request) {
        // Handle the request here
        System.out.println("Request received: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println(request);
//        gitLabService.getAllFilesFromRepo();
        gitService.cloneRepository();

        return ResponseEntity.ok("Request received");
    }

    @GetMapping("/generate-openapi")
    public FileSystemResource generateOpenApi() throws IOException {
        OpenAPI openAPI = new OpenAPI();
        openAPI.path("/api/contracts/generate", new PathItem().post(new Operation()));
        String yaml = Yaml.pretty(openAPI);

        File file = new File("openapi.yml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(yaml);
        }

        return new FileSystemResource(file);
    }
}
