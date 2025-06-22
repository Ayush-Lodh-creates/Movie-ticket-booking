package service;

import entity.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MovieService {

    private Map<Integer, Movie> movies;
    private AtomicInteger movieCounter;

    public MovieService() {
        this.movies = new HashMap<>();
        this.movieCounter = new AtomicInteger(0);
    }

    public Movie getMovie(int movieId) throws Exception {
        if(!movies.containsKey(movieId)) {
            throw new Exception("Movie with id : " + movieId + " not found");
        }
        return movies.get(movieId);
    }

    public Movie createMovie(String movieName, int durationInMinutes) {
        int movieId = movieCounter.incrementAndGet();
        Movie movie = new Movie(movieId, movieName, durationInMinutes);
        movies.put(movieId, movie);
        return movie;
    }
}
