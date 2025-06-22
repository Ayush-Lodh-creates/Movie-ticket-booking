package controller;

import entity.Movie;
import entity.Screen;
import entity.Seat;
import entity.Show;
import service.MovieService;
import service.SeatAvailabilityService;
import service.ShowService;
import service.TheatreService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowController {
    // Dependencies injected for handling show operations, theatre data, movie data, and seat availability.
    private final SeatAvailabilityService seatAvailabilityService;
    private final ShowService showService;
    private final TheatreService theatreService;
    private final MovieService movieService;

    // Constructor to inject all required services
    public ShowController(SeatAvailabilityService seatAvailabilityService, ShowService showService,
                          TheatreService theatreService, MovieService movieService) {
        this.seatAvailabilityService = seatAvailabilityService;
        this.showService = showService;
        this.theatreService = theatreService;
        this.movieService = movieService;
    }

    public int createShow(final int movieId, final int screenId, final Date startTime,
                          final Integer durationInSeconds) throws Exception{
        final Screen screen = theatreService.getScreen(screenId);
        final Movie movie = movieService.getMovie(movieId);
        return showService.createShow(movie, screen, startTime, durationInSeconds).getId();

    }

    public List<Integer> getAvailableSeats(final int showId) throws Exception{
        final Show show = showService.getShow(showId);
        final List<Seat> availableSeats = seatAvailabilityService.getAvailableSeats(show);
        return availableSeats.stream().map(Seat::getSeatId).collect(Collectors.toList());
    }
}
