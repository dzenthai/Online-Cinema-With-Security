/**
 * CREATE TABLE online_cinema.movies
 * (
 * id             SERIAL PRIMARY KEY NOT NULL,
 * title          VARCHAR(256),
 * genre          VARCHAR(64),
 * description    TEXT,
 * release_date   DATE,
 * average_rating FLOAT,
 * subscribe_only BOOLEAN
 * );
 */

package org.online.cinema.movie.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.online.cinema.actor.entity.Actor;
import org.online.cinema.director.entity.Director;
import org.online.cinema.user.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(schema = "online_cinema", name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "genre")
    private String genre;
    @Column(name = "description")
    private String description;
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date releaseDate;
    @Column(name = "average_rating")
    private float rating;
    @Column(name = "subscribe_only")
    private boolean subscribeOnly;
    /**
     * CREATE TABLE online_cinema.movie_actors
     * (
     * movie_id BIGINT,
     * actor_id BIGINT,
     * FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id),
     * FOREIGN KEY (actor_id) REFERENCES online_cinema.actors (id)
     * );
     */
    // For movie_actors table
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(schema = "online_cinema", name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> actors;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    /**
     * CREATE TABLE online_cinema.movie_directors
     * (
     * movie_id    BIGINT,
     * director_id BIGINT,
     * FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id),
     * FOREIGN KEY (director_id) REFERENCES online_cinema.directors (id)
     * );
     */
    // For movie_directors table
    @JoinTable(schema = "online_cinema", name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id"))
    private List<Director> directors;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    /**
     * CREATE TABLE online_cinema.user_preferred
     * (
     * id                 SERIAL PRIMARY KEY NOT NULL,
     * user_id            BIGINT,
     * movie_id           BIGINT,
     * FOREIGN KEY (user_id) REFERENCES online_cinema.users(id),
     * FOREIGN KEY (movie_id) REFERENCES online_cinema.movies(id)
     * );
     */
    // For user_preferred table
    @JoinTable(schema = "online_cinema", name = "user_preferred",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private User users;
}
