package com.starfish.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Data
public class ParseResultMapDTO {
    @JsonIgnore
    private ResponseEntity<String> responseEntity;
    private Map<String, Set<String>> responseMap;
}
