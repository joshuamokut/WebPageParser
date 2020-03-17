package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;

import java.util.Map;
import java.util.Set;

public interface DataAccessService {
    void saveNewWebPageData(Map<String, Set<String>> newData);

    WebPageSetDTO findWebPageData(String webLink);
}
