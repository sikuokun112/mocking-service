package io.featurehub.examples.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class OpenApiController {

    @Autowired
    private OpenApiResource openApiResource;

    @Autowired
    private ObjectMapper objectMapper;

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
