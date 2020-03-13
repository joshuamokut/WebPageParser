package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service

public class WebPageServiceImpl implements WebPageService {

    private Map<String, Set<String>> getLinks(String weblink, int size) throws IOException {
        Map<String, Set<String>> results = new HashMap<>();

        Queue<String> linkQueue = new PriorityQueue<>();
        Set<String> visitedLinks = new HashSet<>();

        linkQueue.add(weblink);
        visitedLinks.add(weblink);

        while (!linkQueue.isEmpty() && size > 0) {
            String currentLink = linkQueue.peek();
            linkQueue.remove(currentLink);

            Document document = Jsoup.connect(weblink).get();
            Elements links = document.select("a[href]");

            for (Element link : links) {
                String externalLink = link.attr("abs:href");
                if (!visitedLinks.contains(externalLink)) {
                    results.get(currentLink).add(externalLink);
                    size--;
                    visitedLinks.add(externalLink);
                    linkQueue.add(externalLink);
                }
            }
        }

        return results;
    }

    @Override
    public ResponseEntity<String> requestParse(String weblink, int size) {
        try {
            Map<String, Set<String>> parseResults = getLinks(weblink, size);

            String url = "localhost:8080/backend/add";
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<Map<String, Set<String>>> request = new HttpEntity<>(parseResults);

            return restTemplate.postForEntity(url, request, String.class);

        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred. PLease check the web link", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<WebPageSetDTO> findParsedResult(String weblink) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "localhost:8080/backend/find";
        Map<String, String> variables = new HashMap<>();
        variables.put("weblink", weblink);
        WebPageSetDTO webPageSetDTO =
                restTemplate.getForObject(url, WebPageSetDTO.class, variables);

        assert webPageSetDTO != null;

        return new ResponseEntity<>(webPageSetDTO, webPageSetDTO.getHttpStatus());
    }
}
