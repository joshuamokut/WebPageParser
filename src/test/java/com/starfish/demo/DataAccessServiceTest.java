package com.starfish.demo;

import com.starfish.demo.DTO.WebPageSetDTO;
import com.starfish.demo.entities.WebPage;
import com.starfish.demo.repositories.WebPageRepository;
import com.starfish.demo.services.DataAccessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class DataAccessServiceTest {

    @Mock
    WebPageRepository webPageRepository;

    @Mock
    HashMap<String, WebPage> webPageMemoize;

    String weblink;
    WebPage webpage;

    String string1;
    WebPage webPage1;

    String string2;
    WebPage webPage2;

    String string3;
    DataAccessServiceImpl dataAccessService;


    Map<String, Set<String>> newData;

    @BeforeEach
    public void init() {
        weblink = "https://google.com";
        webpage = new WebPage(weblink);

        string1 = "https://wikipedia.org";
        webPage1 = new WebPage(string1);

        string2 = "https://amazon.com";
        webPage2 = new WebPage(string2);

        string3 = "http://starfish.com";

        dataAccessService = new DataAccessServiceImpl(webPageMemoize, webPageRepository);
        webpage.setId(1L);
        webPage1.setId(50L);
        webPage2.setId(25L);

        Set<String> webpageSet = new HashSet<>();
        Set<String> webpage1Set = new HashSet<>();
        Set<String> webpage2Set = new HashSet<>();

        webpageSet.add(string1);
        webpageSet.add(string2);
        webpage1Set.add(weblink);
        webpage1Set.add(string2);
        webpage2Set.add(string1);
        webpage2Set.add(weblink);

        newData = new HashMap<>();
        newData.put(weblink, webpageSet);
        newData.put(string1, webpage1Set);
        newData.put(string2, webpage2Set);

        when(webPageRepository.findFirstByWebLink(weblink)).thenReturn(webpage);
        when(webPageRepository.findFirstByWebLink(string1)).thenReturn(webPage1);
        when(webPageRepository.findFirstByWebLink(string2)).thenReturn(webPage2);
        when(webPageRepository.findFirstByWebLink(string3)).thenReturn(null);

    }

    void testFindWebPageDate() {

        WebPageSetDTO webPageSetDTO = dataAccessService.findWebPageData(weblink);
        WebPageSetDTO webPageSetDTO1 = dataAccessService.findWebPageData(string1);
        WebPageSetDTO webPageSetDTO2 = dataAccessService.findWebPageData(string2);
        WebPageSetDTO webPageSetDTO3 = dataAccessService.findWebPageData(string3);

        assert webPageSetDTO.getHttpStatus() == HttpStatus.OK;
        assert webPageSetDTO1.getHttpStatus() == HttpStatus.OK;
        assert webPageSetDTO2.getHttpStatus() == HttpStatus.OK;
        assert webPageSetDTO3.getHttpStatus() == HttpStatus.BAD_REQUEST;

        assert webPageSetDTO.getWebPages().contains(webPage1);
        assert webPageSetDTO.getWebPages().contains(webPage2);
        assert webPageSetDTO1.getWebPages().contains(webpage);
        assert webPageSetDTO1.getWebPages().contains(webPage2);
        assert webPageSetDTO2.getWebPages().contains(webPage1);
        assert webPageSetDTO2.getWebPages().contains(webpage);

        assert webPageSetDTO3.getWebPages().isEmpty();
        assert webPageSetDTO.getWebPages().size() == 2;
        assert webPageSetDTO1.getWebPages().size() == 2;
        assert webPageSetDTO2.getWebPages().size() == 2;
    }

    @Test
    public void testSaveNewWebPageDate() {
        assert !webpage.getExternalLinks().contains(webPage1);
        assert !webpage.getExternalLinks().contains(webPage2);
        assert !webPage1.getExternalLinks().contains(webpage);
        assert !webPage1.getExternalLinks().contains(webPage2);
        assert !webPage2.getExternalLinks().contains(webPage1);
        assert !webPage2.getExternalLinks().contains(webpage);

        dataAccessService.saveNewWebPageData(newData);

        testFindWebPageDate();
    }
}
