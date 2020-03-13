package com.starfish.demo.controllers;


import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.services.WebPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webpage")
public class WebPageController {
    WebPageService webPageService;

    @GetMapping("find")
    ResponseEntity<WebPageSetDTO> findParsedResult(@RequestParam String weblink) {
        return webPageService.findParsedResult(weblink);
    }

    @GetMapping("add")
    ResponseEntity<String> addParseResult(@RequestParam String weblink,
                                          @RequestParam int size) {
        return webPageService.requestParse(weblink, size);
    }
}
