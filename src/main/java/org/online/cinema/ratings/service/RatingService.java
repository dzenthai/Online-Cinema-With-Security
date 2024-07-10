package org.online.cinema.ratings.service;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.dto.RatingInputDTO;
import org.online.cinema.common.dto.RatingOutputDTO;
import org.online.cinema.common.exception.MovieException;
import org.online.cinema.common.exception.RatingException;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.movie.entity.Movie;
import org.online.cinema.movie.repo.MovieRepository;
import org.online.cinema.ratings.entity.Rating;
import org.online.cinema.ratings.repo.RatingRepository;
import org.online.cinema.security.service.ContextHolderService;
import org.online.cinema.user.entity.User;
import org.online.cinema.user.entity.UserInfo;
import org.online.cinema.user.repo.UserInfoRepository;
import org.online.cinema.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RatingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ContextHolderService contextHolder;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public List<RatingOutputDTO> getRatingsByMovieId(Long movieId) {

        List<Rating> ratings = ratingRepository.findByMovieId(movieId);

        if (ratings.isEmpty()) {
            log.error("Rating with movie id={} not found in database", movieId);
            throw new RatingException("Rating with movie id=%s not found in database".formatted(movieId));
        } else {

            log.info("Getting rating for movie with id={} from database", movieId);
            return ratings.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
    }

    @Transactional
    public Rating saveRating(Long id, RatingInputDTO ratingInputDTO) {

        String email = contextHolder.getCurrentEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException("User with email=%s not found in database".formatted(email)));

        Movie movie = movieRepository.findById(id).orElseThrow(() ->
                new MovieException("Movie with id=%s not found in database".formatted(id)));

        List<Rating> existingRatings = ratingRepository.findByMovieIdAndUserId(id, user.getId());

        Rating ratingToUpdate = null;

        if (existingRatings.isEmpty()) {
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            ratingToUpdate = Rating.builder()
                    .movie(movie)
                    .user(user)
                    .review(ratingInputDTO.getReview())
                    .rating(ratingInputDTO.getRating())
                    .date(date)
                    .build();

            ratingToUpdate = ratingRepository.save(ratingToUpdate);
            log.info("Saving rating with id={} to rating database", ratingToUpdate.getId());
        } else {
            ratingToUpdate = existingRatings.getFirst();

            ratingToUpdate.setReview(ratingInputDTO.getReview());
            ratingToUpdate.setRating(ratingInputDTO.getRating());
            ratingToUpdate.setDate(new Date());

            ratingToUpdate = ratingRepository.save(ratingToUpdate);

            log.info("Updating rating with id={} to rating database", ratingToUpdate.getId());
        }

        List<Rating> ratings = ratingRepository.findByMovieId(id);
        float totalRating = 0;
        for (Rating r : ratings) {
            totalRating += r.getRating();
        }

        float averageRating = ratings.isEmpty() ? 0 : totalRating / ratings.size();
        movie.setRating(averageRating);

        log.info("Setting the average rating for the movie={}, average rating={}", movie.getId(), averageRating);

        movieRepository.save(movie);

        return ratingToUpdate;
    }

    private RatingOutputDTO convertToDTO(Rating rating) {

        UserInfo userInfo = userInfoRepository.findUserByUser(rating.getUser());

        log.info("Converting rating for the movie={}, to DTO", rating.getMovie().getTitle());
        return RatingOutputDTO.builder()
                .username(userInfo.getUsername())
                .movie_name(rating.getMovie().getTitle())
                .review(rating.getReview())
                .rating(rating.getRating())
                .date(rating.getDate())
                .build();
    }
}
