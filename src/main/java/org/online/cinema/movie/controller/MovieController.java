package org.online.cinema.movie.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Main Movie Controller")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Operation(
            summary = "Retrieve a movie by id from the database.",
            description = "This method allows you to retrieve a movie by id" +
                    " from the online_cinema.movies database that you have created."
    )
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

    @Operation(
            summary = "Retrieve a movie by user preferences from the database.",
            description = "This method allows you to retrieve a movie by user preferences" +
                    " from the online_cinema.movies database that you have created."
    )
    @GetMapping("/movies/preferences")
    public List<MovieDTO> getMoviesByUserPreference() {
        log.info("Getting all movies based on user preferences");
        return movieService.getMoviesByUserPreference();
    }

    @Operation(
            summary = "Retrieve all movies by genre from the database.",
            description = "This method allows you to retrieve all movies by genre" +
                    " from the online_cinema.movies database that you have created."
    )
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

    @Operation(
            summary = "Retrieve all movies with subscription from the database.",
            description = "This method allows you to retrieve all movies with subscription" +
                    " from the online_cinema.movies database that you have created."
    )
    @GetMapping("/movies/subscription")
    public List<MovieDTO> getMoviesSubscriptionRequired() {
        log.info("Getting movies with subscription");
        return movieService.getMoviesSubscriptionRequired();
    }

    @Operation(
            summary = "Retrieve all free movies from the database.",
            description = "This method allows you to retrieve all free movies" +
                    " from the online_cinema.movies database that you have created."
    )
    @GetMapping("/movies/free")
    public List<MovieDTO> getMoviesNoSubscriptionRequired() {
        log.info("Getting movies with no subscription");
        return movieService.getMoviesNoSubscriptionRequired();
    }

    @Operation(
            summary = "Retrieve all movies by newness from the database.",
            description = "This method allows you to retrieve all movies by newness" +
                    " from the online_cinema.movies database that you have created."
    )
    @GetMapping("/movies/newness")
    public List<MovieDTO> getMoviesByNewness() {
        log.info("Getting movies by newness");
        return movieService.getMoviesByNewness();
    }
}
