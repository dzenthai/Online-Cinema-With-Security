package org.online.cinema.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.MovieDTO;
import org.online.cinema.common.exception.MovieException;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.movie.entity.Movie;
import org.online.cinema.movie.repo.MovieRepository;
import org.online.cinema.security.service.ContextHolderService;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ContextHolderService contextHolder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Movie getMovieById(Long id) {

        Movie movie = null;

        Optional<Movie> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            movie = movieOptional.get();
        }
        log.info("Getting movie with id={}", id);

        return movie;
    }

    @Transactional
    public void getMovieByIdForStreaming(Long id) {
        Movie movie = null;
        Optional<Movie> movieOptional = movieRepository.findById(id);
        if(movieOptional.isPresent()) {
            log.info("Getting movie with id={}, for streaming", id);
            movie = movieOptional.get();
        } else {
            log.error("Movie with id={} not found in database", id);
            throw new MovieException("Movie with id=%s not found in database"
                    .formatted(id));
        }
        User user = null;
        String email = this.contextHolder.getCurrentEmail();
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            log.info("Getting user with email={}, for streaming", email);
            user = userOptional.get();
        } else {
            log.error("User with email={} not found in database", email);
            throw new UserException("User with email %s not found".formatted(email));
        }
        log.info("Checking access rights for the user with email={}", email);
        if (Objects.requireNonNull(movie).isSubscribeOnly()) {
            if (!Objects.requireNonNull(user).getRole().contains("SUBSCRIBED")) {
                log.error("User with email={} does not have access rights", email);
                throw new MovieException(("Access to %s is available only with a subscription." +
                        " Please subscribe to continue.")
                        .formatted(movie.getTitle()));
            }
        }
    }

    @Transactional
    public List<MovieDTO> getMoviesByGenre(String genre) {

        List<Movie> movies = movieRepository.findByGenre(genre)
                .stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .toList();

        log.info("Getting all movies with genre={}, from database", genre);
        return movies.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDTO> getMoviesSubscriptionRequired() {

        List<Movie> movies = movieRepository.findAll()
                .stream()
                .filter(Movie::isSubscribeOnly)
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(5)
                .toList();

        log.info("Getting all movies with subscription required");
        return movies.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDTO> getMoviesNoSubscriptionRequired() {

        List<Movie> movies = movieRepository.findAll()
                .stream()
                .filter(movie -> !movie.isSubscribeOnly())
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(5)
                .toList();

        log.info("Getting all movies without subscription required");
        return movies.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDTO> getMoviesByNewness() {

        List<Movie> movies = movieRepository.findAll().stream()
                .sorted(Comparator.comparing(Movie::getReleaseDate).reversed())
                .limit(10)
                .toList();

        log.info("Getting all movies by newness");
        return movies.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDTO> getMoviesByUserPreference() {

        User user = null;
        String email = this.contextHolder.getCurrentEmail();
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            log.info("Getting user with email={}, to get movies based on user preferences.", email);
            user = userOptional.get();
        } else {
            throw new UserException("User with email %s not found".formatted(email));
        }


        List<String> favoriteGenres = user.getMovies()
                .stream()
                .map(Movie::getGenre)
                .toList();

        List<String> favoriteMovies = user.getMovies()
                .stream()
                .map(Movie::getTitle)
                .toList();

        User finalUser = user;

        List<Movie> movies = movieRepository.findAll()
                .stream()
                .filter(movie -> !favoriteMovies.contains(movie.getTitle()))
                .filter(movie -> favoriteGenres.contains(movie.getGenre()))
                .filter(movie -> {
                    if (!finalUser.getRole().contains("SUBSCRIBED")) {
                        return !movie.isSubscribeOnly();
                    }
                    return favoriteGenres.contains(movie.getGenre()) && !favoriteMovies.contains(movie.getTitle());
                })
                .filter(movie -> movie.getRating()>=7)
                .toList();

        log.info("");

        return movies.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MovieDTO convertToDTO(Movie movie) {
        log.info("Converting movie={}, to DTO", movie.getTitle());
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