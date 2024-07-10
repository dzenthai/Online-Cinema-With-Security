package org.online.cinema.actor.service;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.actor.entity.Actor;
import org.online.cinema.actor.repo.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Transactional
    public Actor getActorById(Long id) {

        Actor actor = null;
        Optional<Actor> actorOptional = actorRepository.findById(id);
        if (actorOptional.isPresent()) {
            actor = actorOptional.get();
        }

        log.info("Getting actor by id={} from database", id);
        return actor;
    }
}
