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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class DataAccessServiceTest {

    @Mock
    WebPageRepository webPageRepository;

    @Mock
    HashMap<String, WebPage> webPageMemoize;

    String string1 = "AAAAA";
    WebPage webPage1 = new WebPage(string1);

    String string2 = "BBBBB";
    WebPage webPage2 = new WebPage(string2);

    String string3 = "CCCCC";
    WebPage webPage3 = new WebPage(string3);

    String string4 = "DDDDDD";
    WebPage webPage4 = new WebPage(string4);

    String string5 = "EEEEE";
    WebPage webPage5 = new WebPage(string5);

    DataAccessServiceImpl dataAccessService;

    @BeforeEach
    void init() {

        dataAccessService = new DataAccessServiceImpl(webPageMemoize, webPageRepository);
    }

    @Test
    void testFindWebPageDate(){
        String weblink="https://google.com";
        WebPage webpage = new WebPage(weblink);
        webpage.setId(1L);

        when(webPageRepository.findFirstByWebLink(weblink)).thenReturn(webpage);

        WebPageSetDTO webPageSetDTO= dataAccessService.findWebPageData(weblink);
    }
}
