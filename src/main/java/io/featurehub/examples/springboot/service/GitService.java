package io.featurehub.examples.springboot.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitService {

    private static final Logger logger = LoggerFactory.getLogger(GitService.class);
    private static final String REMOTE_URL = "https://gitlab.com/sikuokun11/test-events.git";
    private static final String LOCAL_DIR = "./.specmatic/repos/test-events";

    public void cloneRepository() {
        File localPath = new File(LOCAL_DIR);

        // Delete existing directory if it exists
        if (localPath.exists()) {
            deleteDirectory(localPath);
        }

        try {
            // Clone the repository
            Git.cloneRepository()
                    .setURI(REMOTE_URL)
                    .setDirectory(localPath)
                    .call();
            logger.info("Repository cloned to {}", localPath.getAbsolutePath());
        } catch (GitAPIException e) {
            logger.error("Exception occurred while cloning repository", e);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}

