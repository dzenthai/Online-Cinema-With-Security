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

    @GetMapping("/movies/preferences")
    public List<MovieDTO> getMoviesByUserPreference() {
        log.info("Getting all movies based on user preferences");
        return movieService.getMoviesByUserPreference();
    }

    @GetMapping("/movies/genre/{genre}")
    public List<MovieDTO> getMoviesByGenre(@PathVariable String genre) {

        List<MovieDTO> movies = movieService.getMoviesByGenre(genre);

        if (movies.isEmpty()) {
            log.error("Movies with genre={} not found in database", genre);
            throw new MovieException("Movie with genre %s not found in database".formatted(genre));
        } else {

            log.info("Getting movies with genre={} from database", genre);
            return movies;
        }
    }

    @GetMapping("/movies/subscription")
    public List<MovieDTO> getMoviesSubscriptionRequired() {
        log.info("Getting movies with subscription");
        return movieService.getMoviesSubscriptionRequired();
    }

    @GetMapping("/movies/free")
    public List<MovieDTO> getMoviesNoSubscriptionRequired() {
        log.info("Getting movies with no subscription");
        return movieService.getMoviesNoSubscriptionRequired();
    }

    @GetMapping("/movies/newness")
    public List<MovieDTO> getMoviesByNewness() {
        log.info("Getting movies by newness");
        return movieService.getMoviesByNewness();
    }
}
