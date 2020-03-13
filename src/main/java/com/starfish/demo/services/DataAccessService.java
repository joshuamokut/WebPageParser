package com.starfish.demo.services;

import com.starfish.demo.entities.WebPage;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

public interface DataAccessService {
    void saveNewWebPageData(Map<String, Set<String>> newData);
    ResponseEntity<Set<WebPage> >findWebPageData(String webLink);
}
