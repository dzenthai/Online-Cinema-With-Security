/**
 * CREATE TABLE online_cinema.ratings
 * (
 * id       SERIAL PRIMARY KEY NOT NULL,
 * user_id  BIGINT,
 * movie_id BIGINT,
 * review   TEXT,
 * rating   FLOAT,
 * date     DATE,
 * FOREIGN KEY (user_id) REFERENCES online_cinema.users (id),
 * FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id)
 * );
 */


package org.online.cinema.ratings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.online.cinema.movie.entity.Movie;
import org.online.cinema.user.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "online_cinema", name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Column(name = "review")
    private String review;
    @Column(name = "rating")
    private int rating;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    protected Date date;
}
