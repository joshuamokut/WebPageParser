package com.starfish.demo.controllers;


import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.services.WebPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webpage")
@Api(tags = {"Student"})
public class WebPageController {
    private final WebPageService webPageService;

    public WebPageController(WebPageService webPageService) {
        this.webPageService = webPageService;
    }

    @GetMapping("find")
    @ApiOperation(value = "find the result of an already parsed weblink", response = ResponseEntity.class)
    ResponseEntity<WebPageSetDTO> findParsedResult(@RequestParam String weblink) {
        return webPageService.findParsedResult(weblink);
    }

    @GetMapping("add")
    @ApiOperation(value = "submit a link to be parsed", response = ResponseEntity.class)
    ResponseEntity<String> addParseResult(@RequestParam String weblink,
                                          @RequestParam int size) {
        return webPageService.requestParse(weblink, size).getResponseEntity();
    }
}
