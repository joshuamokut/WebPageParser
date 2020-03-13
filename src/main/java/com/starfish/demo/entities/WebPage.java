package com.starfish.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class WebPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NonNull
    private String webLink;

    @OneToMany
    @JsonIgnore
    private Set<WebPage> externalLinks = new HashSet<>();

    public void addExternalLink(WebPage webPage){
        externalLinks.add(webPage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebPage webPage = (WebPage) o;
        return id.equals(webPage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
