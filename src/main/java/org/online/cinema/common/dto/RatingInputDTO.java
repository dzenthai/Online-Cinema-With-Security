package org.online.cinema.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingInputDTO {
    private String review;
    private int rating;
}
