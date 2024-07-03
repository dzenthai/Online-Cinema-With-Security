package org.online.cinema.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.online.cinema.actor.entity.Actor;
import org.online.cinema.director.entity.Director;

@Data
@Entity
@NoArgsConstructor
@Table(schema = "online_cinema", name = "user_preferred")
public class UserPreferred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "preferred_genre")
    private String preferredGenre;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "preferred_actor")
    private Actor actor;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "preferred_director")
    private Director director;
}
