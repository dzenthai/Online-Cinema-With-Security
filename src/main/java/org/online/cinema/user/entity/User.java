/**
 * CREATE TABLE online_cinema.users
 * (
 * id SERIAL PRIMARY KEY,
 * email VARCHAR(64),
 * password VARCHAR,
 * role VARCHAR(128)
 * );
 */

package org.online.cinema.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.online.cinema.movie.entity.Movie;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "online_cinema", name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]" +
            "+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Email should be in the format 'your_email@gmail.com'," +
            " where 'something' can contain any characters and 'domain' should not contain spaces.")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Transient
    private boolean enabled = false;
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
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(schema = "online_cinema", name = "user_preferred",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @JsonManagedReference
    private List<Movie> movies;

    public void addMovie(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
    }
}
