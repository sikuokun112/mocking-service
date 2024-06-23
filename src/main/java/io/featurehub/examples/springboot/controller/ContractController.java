package io.featurehub.examples.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @PostMapping("/generate")
    public ResponseEntity<String> generateContractAndStub(@RequestBody ContractRequest request) {
        try {

            String folderPath = "contracts/" + request.getFolderName();
            Path folder = Paths.get(folderPath);

            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            // Write the api-contract.yml file
            Path contractFile = folder.resolve("api-contract.yml");
            Files.write(contractFile, request.getApiContract().getBytes());

            // Write the stub.json file
            Path stubFile = folder.resolve("stub.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(stubFile.toFile(), request.getStub());
            return ResponseEntity.ok("Contract and stub files generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to generate contract and stub files.");
        }
    }

    static class ContractRequest {
        private String folderName;
        private String apiContract;
        private StubRequestResponse stub;

        // Getters and setters...

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public String getApiContract() {
            return apiContract;
        }

        public void setApiContract(String apiContract) {
            this.apiContract = apiContract;
        }

        public StubRequestResponse getStub() {
            return stub;
        }

        public void setStub(StubRequestResponse stub) {
            this.stub = stub;
        }
    }

    static class StubRequestResponse {
        private StubRequest httpRequest;
        private StubResponse httpResponse;

        // Getters and setters...

        public StubRequest getHttpRequest() {
            return httpRequest;
        }

        public void setHttpRequest(StubRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        public StubResponse getHttpResponse() {
            return httpResponse;
        }

        public void setHttpResponse(StubResponse httpResponse) {
            this.httpResponse = httpResponse;
        }
    }

    static class StubRequest {
        private String method;
        private String path;
        private Map<String, Object> query;

        // Getters and setters...

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map<String, Object> getQuery() {
            return query;
        }

        public void setQuery(Map<String, Object> query) {
            this.query = query;
        }
    }

    static class StubResponse {
        private int status;
        private String body;

        // Getters and setters...

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
