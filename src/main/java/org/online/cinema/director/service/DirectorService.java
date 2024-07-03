package org.online.cinema.director.service;

import org.online.cinema.director.repo.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

}
