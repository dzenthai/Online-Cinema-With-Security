CREATE TABLE online_cinema.users
(
    id       SERIAL PRIMARY KEY NOT NULL,
    email    VARCHAR(64) UNIQUE,
    password VARCHAR(128),
    role     VARCHAR(128)
);

CREATE TABLE online_cinema.users_info
(
    id                SERIAL PRIMARY KEY,
    user_id           BIGINT,
    username          VARCHAR(128),
    gender            VARCHAR(16),
    is_subscribed     BOOLEAN,
    registration_date DATE,
    FOREIGN KEY (user_id) REFERENCES online_cinema.users (id)
);

CREATE TABLE online_cinema.movies
(
    id             SERIAL PRIMARY KEY NOT NULL,
    title          VARCHAR(256),
    genre          VARCHAR(64),
    description    TEXT,
    release_date   DATE,
    average_rating FLOAT,
    subscribe_only BOOLEAN
);

CREATE TABLE online_cinema.actors
(
    id           SERIAL PRIMARY KEY NOT NULL,
    first_name   VARCHAR(128),
    last_name    VARCHAR(128),
    age          INT,
);

CREATE TABLE online_cinema.directors
(
    id           SERIAL PRIMARY KEY NOT NULL,
    first_name   VARCHAR(128),
    last_name    VARCHAR(128),
    age          INT,
);

CREATE TABLE online_cinema.movie_actors
(
    movie_id BIGINT,
    actor_id BIGINT,
    FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id),
    FOREIGN KEY (actor_id) REFERENCES online_cinema.actors (id)
);

CREATE TABLE online_cinema.movie_directors
(
    movie_id    BIGINT,
    director_id BIGINT,
    FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id),
    FOREIGN KEY (director_id) REFERENCES online_cinema.directors (id)
);

CREATE TABLE online_cinema.ratings
(
    id       SERIAL PRIMARY KEY NOT NULL,
    user_id  BIGINT,
    movie_id BIGINT,
    review   TEXT,
    rating   FLOAT,
    date     DATE,
    FOREIGN KEY (user_id) REFERENCES online_cinema.users (id),
    FOREIGN KEY (movie_id) REFERENCES online_cinema.movies (id)
);

CREATE TABLE online_cinema.user_preferred
(
    id                 SERIAL PRIMARY KEY NOT NULL,
    user_id            BIGINT,
    movie_id           BIGINT,
    FOREIGN KEY (user_id) REFERENCES online_cinema.users(id),
    FOREIGN KEY (movie_id) REFERENCES online_cinema.movies(id)
);