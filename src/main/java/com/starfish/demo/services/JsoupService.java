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
        return Jsoup.connect(weblink).userAgent("Mozilla").get();
        //Ok Jsoup fails to fetch some url because some sites have a sniffing agent
        //and they can figure out that it's java trying to access their site. So
        // they restrict access to their site. That's why we have to use UserAgent

    }

}
