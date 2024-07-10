package org.online.cinema.ratings.repo;

import org.online.cinema.ratings.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);

    List<Rating> findByMovieIdAndUserId(Long movieId, Long userId);
}
