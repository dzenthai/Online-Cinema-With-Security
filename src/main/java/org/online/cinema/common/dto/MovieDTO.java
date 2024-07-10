package org.online.cinema.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MovieDTO {
    private String title;
    private String genre;
    private String description;
    private Date release_date;
    private float rating;
    private boolean subscribe_only;
}
