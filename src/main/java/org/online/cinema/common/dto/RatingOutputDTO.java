package org.online.cinema.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RatingOutputDTO {
    private String user_email;
    private String movie_name;
    private String review;
    private int rating;
    private Date date;
}
