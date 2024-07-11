/**
 *CREATE TABLE online_cinema.actors
 * (
 * id           SERIAL PRIMARY KEY NOT NULL,
 * first_name   VARCHAR(128),
 * last_name    VARCHAR(128),
 * age          INT,
 * );
 */

package org.online.cinema.actor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.online.cinema.movie.entity.Movie;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(schema = "online_cinema", name = "actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "age")
    private int age;
    /**
     * CREATE TABLE online_cinema.movie_actors
     * (
     * movie_id BIGINT,
     * actor_id BIGINT,
     * FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id),
     * FOREIGN KEY (actor_id) REFERENCES online_cinema.actors (id)
     * );
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(schema = "online_cinema", name = "movie_actors",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;
}
