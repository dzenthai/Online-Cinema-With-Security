package org.online.cinema.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.MovieDTO;
import org.online.cinema.common.exception.MovieException;
import org.online.cinema.movie.entity.Movie;
import org.online.cinema.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movie/{id}")
    public MovieDTO getMovieById(@PathVariable Long id) {

        Movie movie = movieService.getMovieById(id);

        if (movie == null) {
            log.error("Movie with id={} not found in database", id);
            throw new MovieException("Movie with id=%s not found in database".formatted(id));
        } else {

            log.info("Movie with id={} found in database", id);
            return MovieDTO.builder()
                    .title(movie.getTitle())
                    .genre(movie.getGenre())
                    .description(movie.getDescription())
                    .release_date(movie.getReleaseDate())
                    .rating(movie.getRating())
                    .subscribe_only(movie.isSubscribeOnly())
                    .build();
        }
    }

    @GetMapping("/movies")
    public List<MovieDTO> getAllMovies() {
        log.info("Getting all movies from database");
        return movieService.getAllMovies()
                .stream()
                .sorted(Comparator.comparing(MovieDTO::getRating).reversed())
                .toList();
    }

    @GetMapping("/movies/{genre}")
    public List<MovieDTO> getMovieByGenre(@PathVariable String genre) {

        List<MovieDTO> movies = movieService.getMovieByGenre(genre);

        if (movies.isEmpty()) {
            log.error("Movies with genre={} not found in database", genre);
            throw new MovieException("Movie with genre %s not found in database".formatted(genre));
        } else {

            log.info("Getting movies with genre={} from database", genre);
            return movies;
        }
    }

    @GetMapping("/movies/subscription")
    public List<MovieDTO> getMovieSubscriptionRequired() {
        log.info("Getting movies with subscription");
        return movieService.getMovieSubscriptionRequired();
    }

    @GetMapping("/movies/free")
    public List<MovieDTO> getMovieNoSubscriptionRequired() {
        log.info("Getting movies with no subscription");
        return movieService.getMovieNoSubscriptionRequired();
    }
}
