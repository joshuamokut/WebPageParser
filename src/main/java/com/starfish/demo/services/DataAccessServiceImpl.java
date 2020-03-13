package com.starfish.demo.services;

import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.entities.WebPage;
import com.starfish.demo.repositories.WebPageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DataAccessServiceImpl implements DataAccessService {
    // this is just an optimization fix just like caching
    private static Map<String, WebPage> webPageMemoize =
            new HashMap<>();
    @NonNull
    private final WebPageRepository webPageRepository;


    /*

    we actually have two options here:
    1. for every string save all its external links using one
    entity instance... this is probably time efficient because we make less calls to the database
    and retrieving is faster because it's just one query to the database.
    however it isn't memory efficient because we have to save one link over and over again for
    many ancestors.

    2. save this as a DAG structure. this is less time efficient than the first but optimized for
    memory performance. memory is O(N) in worst case where N is the number of links processed and
    stored in our database, compared to the previous method where in the worst case we use O(N^2)
     memory and


     */

    @Override
    public void saveNewWebPageData(Map<String, Set<String>> newData) {
        for (String webLink : newData.keySet()) {
            WebPage webPage = getWebPageEntity(webLink);

            for (String externalLink : newData.get(webLink)) {
                WebPage externalPage = getWebPageEntity(externalLink);
                if (!externalPage.equals(webPage)) {
                    webPage.addExternalLink(externalPage);
                }
            }
        }

        webPageRepository.flush();
    }

    private WebPage getWebPageEntity(String webLink) {
        //this optimization allows for amortized time complexity of (O(N) where N is the number
        //of links processed and stored.
        WebPage webPage = webPageMemoize.containsKey(webLink) ?
                webPageMemoize.get(webLink) :
                webPageRepository.findFirstByWebLink(webLink);

        if (webPage == null) {
            webPage = new WebPage(webLink);

            webPageRepository.save(webPage);
        }

        webPageMemoize.putIfAbsent(webLink, webPage);

        return webPage;
    }

    @Override
    public WebPageSetDTO findWebPageData(String webLink) {
        WebPage webPage = webPageRepository.findFirstByWebLink(webLink);

        if (webPage == null)
            return new WebPageSetDTO(new HashSet<>(), HttpStatus.BAD_REQUEST);

        Set<WebPage> resultSet = new HashSet<>();
        Queue<WebPage> webPageQueue = new PriorityQueue<>();
        Set<WebPage> visited = new HashSet<>();

        visited.add(webPage);
        webPageQueue.add(webPage);

        while (!webPageQueue.isEmpty()) {
            WebPage frontPage = webPageQueue.peek();
            webPageQueue.remove(frontPage);
            resultSet.add(frontPage);

            for (WebPage page : frontPage.getExternalLinks()) {
                if (!visited.contains(page)) {
                    visited.add(page);
                    webPageQueue.add(page);
                }
            }
        }

        return new WebPageSetDTO(resultSet, HttpStatus.OK);

    }

}
