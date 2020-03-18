package com.starfish.demo.services;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@NoArgsConstructor
@Component
public class JsoupService {
    public Document getDoc(String weblink) throws IOException {
        return Jsoup.connect(weblink).timeout(30000).userAgent("Mozilla/17.0").get();
    }

}
