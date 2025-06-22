package service;

import entity.Movie;
import entity.Screen;
import entity.Show;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowService {

    private Map<Integer, Show> shows;
    private AtomicInteger showCounter;

    public ShowService() {
        this.shows = new HashMap<>();
        this.showCounter = new AtomicInteger(0);
    }

    public Show getShow(int showId) throws Exception {
        if(!shows.containsKey(showId)) {
            throw new Exception("Show with ID " + showId + " not found.");
        }
        return shows.get(showId);
    }

    public Show createShow(Movie movie, Screen screen, Date startTime, Integer durationInSeconds) {
        int showId = showCounter.incrementAndGet();
        Show show = new Show(showId, movie, screen, startTime, durationInSeconds);
        shows.put(showId, show);
        return show;
    }

    public List<Show> getShowForScreen(Screen screen) {
        final List<Show> response = new ArrayList<>();
        for(Show show : shows.values()) {
            if(show.getScreen().getScreenId() == screen.getScreenId()) {
                response.add(show);
            }
        }
        return response;
    }
}
