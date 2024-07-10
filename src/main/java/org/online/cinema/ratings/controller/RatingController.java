package org.online.cinema.ratings.controller;

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
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/movie/ratings/{id}")
    public List<RatingOutputDTO> getRatingsByMovieId(@PathVariable Long id) {
        log.info("Getting ratings by movie with id={}", id);
        return ratingService.getRatingsByMovieId(id);
    }

    @PostMapping("/movie/rate/{id}")
    public Rating saveRating(@PathVariable Long id, @RequestBody RatingInputDTO ratingInputDTO) {
        log.info("Saving rating for movie with id={}", id);
        return ratingService.saveRating(id, ratingInputDTO);
    }

    @PutMapping("/movie/rate/{id}")
    public Rating updateRating(@PathVariable Long id, @RequestBody RatingInputDTO ratingInputDTO) {
        log.info("Updating rating for movie with id={}", id);
        return ratingService.saveRating(id, ratingInputDTO);
    }
}
