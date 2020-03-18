package com.starfish.demo;

import com.starfish.demo.entities.WebPage;
import com.starfish.demo.services.JsoupService;
import com.starfish.demo.services.RestTemplateService;
import com.starfish.demo.services.WebPageServiceImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class WebPageServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    JsoupService jsoupService;

    @Mock
    Document document;

    @Mock
    Document document1;

    @Mock
    Document document2;


    Elements elements;

    Elements elements1;

    Elements elements2;

    @Mock
    Element element;
    @Mock
    Element element1;
    @Mock
    Element element2;

    String weblink;
    WebPage webpage;

    String string1;
    WebPage webPage1;

    String string2;
    WebPage webPage2;

    String string3;
    WebPageServiceImpl webPageService;

    @BeforeEach
    public void init() throws IOException {
        weblink = "https://google.com";
        webpage = new WebPage(weblink);

        string1 = "https://wikipedia.org";
        webPage1 = new WebPage(string1);

        string2 = "https://amazon.com";
        webPage2 = new WebPage(string2);

        string3 = "http://starfish.com";

        webPageService = new WebPageServiceImpl(jsoupService, restTemplateService);

        webpage.setId(1L);
        webPage1.setId(50L);
        webPage2.setId(25L);

        elements = new Elements();
        elements1 = new Elements();
        elements2 = new Elements();

        elements.add(element1);
        elements.add(element2);
        elements1.add(element);
        elements1.add(element2);
        elements2.add(element);
        elements2.add(element1);


        lenient().when(jsoupService.getDoc(weblink)).thenReturn(document);
        lenient().when(jsoupService.getDoc(string1)).thenReturn(document1);
        lenient().when(jsoupService.getDoc(string2)).thenReturn(document2);

        lenient().when(document.select("a[href]")).thenReturn(elements);
        lenient().when(document1.select("a[href]")).thenReturn(elements1);
        lenient().when(document2.select("a[href]")).thenReturn(elements2);
        lenient().when(element.attr("abs:href")).thenReturn(weblink);
        lenient().when(element1.attr("abs:href")).thenReturn(string1);
        lenient().when(element2.attr("abs:href")).thenReturn(string2);
        lenient().when(restTemplateService.postExchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(String.class))).thenReturn(ResponseEntity.ok("it's cool"));
    }

    @Test
    public void testGetLinks() {
        Map<String, Set<String>> response = webPageService.requestParse(weblink, 1).getResponseMap();

        for (String key : response.keySet()) {
            System.out.println("key: " + key);
            for (String value : response.get(key)) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        response = webPageService.requestParse(string1, 2).getResponseMap();

        System.out.println("------------------");
        for (String key : response.keySet()) {
            System.out.println("key: " + key);
            for (String value : response.get(key)) {
                System.out.print(value + " ");
            }

            System.out.println();
        }

        response = webPageService.requestParse(string2, 10).getResponseMap();

        System.out.println("------------------");
        for (String key : response.keySet()) {
            System.out.println("key: " + key);
            for (String value : response.get(key)) {
                System.out.print(value + " ");
            }
        }

    }
}
