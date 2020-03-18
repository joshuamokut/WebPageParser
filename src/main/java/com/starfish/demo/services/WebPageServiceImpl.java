package com.starfish.demo.services;

import com.starfish.demo.DTO.ParseResultMapDTO;
import com.starfish.demo.DTO.WebPageSetDTO;
import org.jsoup.nodes.Document;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class WebPageServiceImpl implements WebPageService {

    private final JsoupService jsoupService;

    private final RestTemplateService restTemplateService;

    public WebPageServiceImpl(JsoupService jsoupService, RestTemplateService restTemplateService) {
        this.jsoupService = jsoupService;
        this.restTemplateService = restTemplateService;
    }


    private Map<String, Set<String>> getLinks(String weblink, int size) throws IOException {
        Map<String, Set<String>> results = new HashMap<>();

        Queue<String> linkQueue = new PriorityQueue<>();
        Set<String> visitedLinks = new HashSet<>();

        linkQueue.add(weblink);
        visitedLinks.add(weblink);

        while (!linkQueue.isEmpty() && size > 0) {
            String currentLink = linkQueue.peek();
            linkQueue.remove(currentLink);

            Document document = jsoupService.getDoc(currentLink);
            List<String> links = document.select("a[href]").stream().map(
                    element -> element.attr("abs:href")).collect(Collectors.toList());

            for (String externalLink : links) {
                if (!visitedLinks.contains(externalLink)) {
                    if (results.get(currentLink) == null) {
                        Set<String> set = new HashSet<>();
                        set.add(externalLink);
                        results.put(currentLink, set);
                    } else {
                        results.get(currentLink).add(externalLink);
                    }
                    size--;
                    visitedLinks.add(externalLink);
                    linkQueue.add(externalLink);
                }

                if (size == 0)
                    break;
            }
        }

        return results;
    }

    @Override
    public ParseResultMapDTO requestParse(String weblink, int size) {
        try {
            Map<String, Set<String>> parseResults = getLinks(weblink, size);

            String url = "http://localhost:8080/backend/add";
            HttpEntity<Map<String, Set<String>>> request = new HttpEntity<>(parseResults);

            restTemplateService.postExchange(url, HttpMethod.POST, request, String.class);
            return new ParseResultMapDTO(ResponseEntity.ok("All good, result added"), parseResults);

        } catch (IOException e) {
            e.printStackTrace();
            return new ParseResultMapDTO(new ResponseEntity<>("Something went wrong, please check the url",
                    HttpStatus.BAD_REQUEST),
                    new HashMap<>());
        }
    }

    @Override
    public ResponseEntity<WebPageSetDTO> findParsedResult(String weblink) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/backend/find";

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("weblink", weblink);

        HttpEntity<WebPageSetDTO> httpEntity = new HttpEntity<>(new HttpHeaders());


        return restTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.GET, httpEntity, WebPageSetDTO.class);
    }
}
