package controller;

import entity.Seat;
import entity.Show;
import entity.User;
import service.BookingService;
import service.ShowService;
import service.TheatreService;

import java.util.ArrayList;
import java.util.List;

public class BookingController {
    private final ShowService showService;
    private final BookingService bookingService;
    private final TheatreService theatreService;

    public BookingController(ShowService showService, BookingService bookingService, TheatreService theatreService) {
        this.showService = showService;
        this.bookingService = bookingService;
        this.theatreService = theatreService;
    }

    public String createBooking(final User user, int showId, List<Integer> seatids) throws Exception {
        Show show = showService.getShow(showId);
        List<Seat> seats = new ArrayList<>();
        for(Integer seatsId: seatids) {
            Seat seat = theatreService.getSeat(seatsId);
            seats.add(seat);
        }
        return bookingService.createBooking(user, show, seats).getId();
    }
}
