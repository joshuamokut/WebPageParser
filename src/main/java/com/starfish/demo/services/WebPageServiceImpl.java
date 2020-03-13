package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.entities.WebPage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service

public class WebPageServiceImpl implements WebPageService {

    @Override
    public void requestParse(String weblink, int size) {

    }

    @Override
    public ResponseEntity<WebPageSetDTO> findParsedResult(String weblink) {
        RestTemplate restTemplate= new RestTemplate();

        String url= "localhost:8080/webpage/find";
        Map<String, String> variables= new HashMap<>();
        variables.put("weblink", weblink);
        WebPageSetDTO webPageSetDTO =
                restTemplate.getForObject(url, WebPageSetDTO.class, variables);

        assert webPageSetDTO != null;
        return new ResponseEntity<>(webPageSetDTO, webPageSetDTO.getHttpStatus());
    }
}
