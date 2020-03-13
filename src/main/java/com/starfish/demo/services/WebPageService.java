package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.entities.WebPage;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface WebPageService {
    void requestParse(String weblink, int size);
    ResponseEntity<WebPageSetDTO> findParsedResult(String weblink);
}
