package com.starfish.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.starfish.demo.entities.WebPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebPageSetDTO {
    private Set<WebPage> webPages ;
    @JsonIgnore
    private HttpStatus httpStatus;
}
