package com.starfish.demo.controllers;


import com.starfish.demo.entities.WebPage;
import com.starfish.demo.services.DataAccessService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/webpage")
public class DataAccessController {
    DataAccessService dataAccessService;

    @GetMapping("/find")
    public ResponseEntity<Set<WebPage>> getExternalLinks(@RequestParam String weblink) {
        return dataAccessService.findWebPageData(weblink);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addResult(@RequestBody Map<String, Set<String>> result) {
        dataAccessService.saveNewWebPageData(result);
        return new ResponseEntity<>("Results added", HttpStatus.CREATED);
    }
}
