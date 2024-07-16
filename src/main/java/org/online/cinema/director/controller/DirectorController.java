package org.online.cinema.director.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.DirectorDTO;
import org.online.cinema.common.dto.MovieDTO;
import org.online.cinema.common.exception.DirectorException;
import org.online.cinema.director.entity.Director;
import org.online.cinema.director.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Director Controller")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    @Operation(
            summary = "Retrieve all directors from the database.",
            description = "This method allows you to retrieve all directors" +
                    " from the online_cinema.directors database that you have created."
    )
    @GetMapping("/directors/{id}")
    public DirectorDTO getDirectorById(@PathVariable Long id) {

        Director director = directorService.getDirectorById(id);

        if (director == null) {
            log.error("Director with id={} not found in database", id);
            throw new DirectorException("Director with id=%s not found in database".formatted(id));
        } else {

            List<MovieDTO> movieDTOs = director.getMovies().stream()
                    .map(movie -> MovieDTO.builder()
                            .title(movie.getTitle())
                            .genre(movie.getGenre())
                            .description(movie.getDescription())
                            .release_date(movie.getReleaseDate())
                            .rating(movie.getRating())
                            .subscribe_only(movie.isSubscribeOnly())
                            .build())
                    .collect(Collectors.toList());

            log.info("Director with id={} found in database", id);
            return DirectorDTO.builder()
                    .first_name(director.getFirstName())
                    .last_name(director.getLastName())
                    .age(director.getAge())
                    .movies(movieDTOs)
                    .build();
        }
    }
}
