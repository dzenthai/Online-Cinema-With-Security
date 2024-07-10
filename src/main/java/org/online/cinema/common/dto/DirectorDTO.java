package org.online.cinema.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DirectorDTO {
    private String first_name;
    private String last_name;
    private int age;
    private List<MovieDTO> movies;
}
