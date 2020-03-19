package com.starfish.demo.repositories;

import com.starfish.demo.entities.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebPageRepository extends JpaRepository<WebPage, Long> {
    WebPage findFirstByWebLink(String webLink);

}
