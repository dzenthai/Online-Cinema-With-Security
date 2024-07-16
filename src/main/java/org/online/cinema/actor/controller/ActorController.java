package org.online.cinema.actor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.online.cinema.actor.entity.Actor;
import org.online.cinema.actor.service.ActorService;
import org.online.cinema.common.dto.ActorDTO;
import org.online.cinema.common.dto.MovieDTO;
import org.online.cinema.common.exception.ActorException;
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
@Tag(name = "Actor Controller")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @Operation(
            summary = "Retrieve all actors from the database.",
            description = "This method allows you to retrieve all actors" +
                    " from the online_cinema.actors database that you have created."
    )
    @GetMapping("/actors/{id}")
    public ActorDTO getActorById(@PathVariable Long id) {

        Actor actor = actorService.getActorById(id);

        if (actor == null) {
            log.error("Actor with id={} not found in database", id);
            throw new ActorException("Actor with id=%s not found in database".formatted(id));
        } else {

            List<MovieDTO> movieDTOs = actor.getMovies().stream()
                    .map(movie -> MovieDTO.builder()
                            .title(movie.getTitle())
                            .genre(movie.getGenre())
                            .description(movie.getDescription())
                            .release_date(movie.getReleaseDate())
                            .rating(movie.getRating())
                            .subscribe_only(movie.isSubscribeOnly())
                            .build())
                    .collect(Collectors.toList());

            log.info("Actor with id={} found in database", id);
            return ActorDTO.builder()
                    .first_name(actor.getFirstName())
                    .last_name(actor.getLastName())
                    .age(actor.getAge())
                    .movies(movieDTOs)
                    .build();
        }
    }
}
