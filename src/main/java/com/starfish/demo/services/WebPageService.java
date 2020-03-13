package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import org.springframework.http.ResponseEntity;

public interface WebPageService {
    ResponseEntity<String> requestParse(String weblink, int size);
    ResponseEntity<WebPageSetDTO> findParsedResult(String weblink);
}
