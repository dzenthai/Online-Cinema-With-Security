package org.online.cinema.director.service;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.director.entity.Director;
import org.online.cinema.director.repo.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    @Transactional
    public Director getDirectorById(Long id) {

        Director director = null;

        Optional<Director> optional = directorRepository.findById(id);

        if (optional.isPresent()) {
            director = optional.get();
        }

        log.info("Getting director by id={} from database", id);
        return director;
    }
}
