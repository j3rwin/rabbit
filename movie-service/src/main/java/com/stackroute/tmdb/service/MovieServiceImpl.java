package com.stackroute.tmdb.service;

import com.stackroute.tmdb.exceptions.MovieAlreadyExistException;
import com.stackroute.tmdb.exceptions.MovieNotFoundException;
import com.stackroute.tmdb.model.Movie;
import com.stackroute.tmdb.repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Primary
@Profile("prod")
public class MovieServiceImpl implements MovieService{
    private MovieRepo movieRepo;

    @Autowired
    public MovieServiceImpl(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public List<Movie> findAll() {
        return movieRepo.findAll();
    }

    public Movie findById(int movieId) throws MovieNotFoundException{
        return movieRepo
                .findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie "+movieId+" not found"));
    }

    public Movie saveMovie(Movie movie) {
        if(existsById(movie.getId())) {
            throw new MovieAlreadyExistException("Movie id: "+movie.getId()+" already exist");
        }
        movieRepo.save(movie);
        return movie;
    }

    public Movie updateMovie(Movie movie){
        movieRepo.save(movie);
        return movie;
    }

    public Movie deleteMovie(int movieId) throws MovieNotFoundException {
        Movie movie =
                movieRepo
                        .findById(movieId)
                        .orElseThrow(() -> new MovieNotFoundException("Movie "+movieId+" not found"));
        movieRepo.delete(movie);
        return movie;
    }

    public boolean existsById(int id) {
        return movieRepo.existsById(id);
    }

    public List<Movie> getMoviesByName(String name) {
        return movieRepo.findByTitleIgnoreCaseContaining(name);
    }

}
