package com.cuupa.classificator.api_client.client;

import com.cuupa.classificator.api_client.model.ClassificationRequest;
import com.cuupa.classificator.api_client.model.ClassificationResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
public class ClassificationClient extends RestClient {

    private final String path = "classification";

    public ClassificationResult classify(ClassificationRequest request) {
        final HttpEntity<ClassificationRequest> entity = new HttpEntity<>(request, headers);

        final ResponseEntity<ClassificationResult> result = new RestTemplate().postForEntity(getUrlToCall(path), entity, ClassificationResult.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return result.getBody();
        }

        return null;
    }
}
