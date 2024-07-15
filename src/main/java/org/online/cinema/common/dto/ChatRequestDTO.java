package org.online.cinema.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRequestDTO {
    private String genre;
    private String actor;
    private String director;
    private String movie_info;
}
