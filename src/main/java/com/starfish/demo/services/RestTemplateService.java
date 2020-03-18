package com.starfish.demo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class RestTemplateService {
    private final RestTemplate restTemplate=new RestTemplate();

    public ResponseEntity postExchange(String url,
                                       HttpMethod httpMethod,
                                       HttpEntity httpEntity,
                                       Class c){
        return restTemplate.exchange(url, httpMethod, httpEntity, c);
    }
}
