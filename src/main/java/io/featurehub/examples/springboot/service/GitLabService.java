package io.featurehub.examples.springboot.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class GitLabService {

    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/projects/";
    private static final String REPO_ID = "59238016";
    private static final String FILE_PATH = "";
    private static final String BRANCH_NAME = "main";
    private static final String PRIVATE_TOKEN = "glpat-5KrJdcP8zh844tXoET64";

    public void getAllFilesFromRepo() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = GITLAB_API_URL + REPO_ID + "/repository/tree?recursive=true";

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map<String, String>> files = response.getBody();

        for (Map<String, String> file : files) {
            if ("blob".equals(file.get("type"))) {
                downloadFile(file.get("path"));
            }
        }
    }

    private void downloadFile(String filePath) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = GITLAB_API_URL + REPO_ID + "/repository/files/" + filePath + "/raw?ref=" + BRANCH_NAME;

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("./" + filePath))) {
            stream.write(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
