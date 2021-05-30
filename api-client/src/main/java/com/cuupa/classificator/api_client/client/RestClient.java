package com.cuupa.classificator.api_client.client;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
public abstract class RestClient {

    @Value("url:localhost:8080")
    protected String url;

    protected final HttpHeaders headers = new HttpHeaders();

    {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    protected String getUrlToCall(String path) {
        if (url.endsWith("/")) {
            return url + "api/rest/v2/" + path;
        }
        return url + "/api/rest/v2/" + path;
    }
}
