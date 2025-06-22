package controller;

import service.MovieService;

public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public int createMovie(String movieName, int durationInMinutes) {
        return movieService.createMovie(movieName, durationInMinutes).getMovieId();
    }
}
