package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> requestParse(String weblink, int size) {
        try {
            Map<String, Set<String>> parseResults = getLinks(weblink, size);

            String url = "http://localhost:8080/backend/add";
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Map<String, Set<String>>> request = new HttpEntity<>(parseResults);
            return restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred. PLease check the web link", HttpStatus.BAD_REQUEST);
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
