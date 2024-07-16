package org.online.cinema.ratings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.RatingInputDTO;
import org.online.cinema.common.dto.RatingOutputDTO;
import org.online.cinema.ratings.entity.Rating;
import org.online.cinema.ratings.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Rating Controller")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Operation(
            summary = "Retrieve all ratings for the movie by movie id.",
            description = "This method allows you to retrieve all ratings for the movie by its id."
    )
    @GetMapping("/movie/ratings/{id}")
    public List<RatingOutputDTO> getRatingsByMovieId(@PathVariable Long id) {
        log.info("Getting ratings by movie with id={}", id);
        return ratingService.getRatingsByMovieId(id);
    }

    @Operation(
            summary = "Save a review of the movie by its id.",
            description = "This method allows the user to save their review of the movie by its id. " +
                    "Based on the rating, for example, if it's above 7, the movie is added to the user's list of favorite movies."
    )
    @PostMapping("/movie/rate/{id}")
    public Rating saveRating(@PathVariable Long id, @RequestBody RatingInputDTO ratingInputDTO) {
        log.info("Saving rating for movie with id={}", id);
        return ratingService.saveRating(id, ratingInputDTO);
    }

    @Operation(
            summary = "Update a review of the movie by its id.",
            description = "This method allows the user to update their review of the movie by its id. " +
                    "Based on the rating, for example, if it's above 7, the movie is added to the user's list of favorite movies."
    )
    @PutMapping("/movie/rate/{id}")
    public Rating updateRating(@PathVariable Long id, @RequestBody RatingInputDTO ratingInputDTO) {
        log.info("Updating rating for movie with id={}", id);
        return ratingService.saveRating(id, ratingInputDTO);
    }
}
