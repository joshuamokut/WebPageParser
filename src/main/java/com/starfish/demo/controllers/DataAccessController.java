package com.starfish.demo.controllers;


import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.services.DataAccessService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/backend")
public class DataAccessController {
    DataAccessService dataAccessService;

    @GetMapping("/find")
    public WebPageSetDTO getExternalLinks(@RequestParam String weblink) {
        return dataAccessService.findWebPageData(weblink);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addResult(@RequestBody Map<String, Set<String>> result) {
        dataAccessService.saveNewWebPageData(result);
        return new ResponseEntity<>("Results added", HttpStatus.CREATED);
    }
}
