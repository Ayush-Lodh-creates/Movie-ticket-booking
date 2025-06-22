package utils.lock_provider;

import entity.Seat;
import entity.SeatLock;
import entity.Show;
import entity.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SeatLockProvider implements ISeatLockProvider {
    
    private Integer lockTimeOut;
    private Map<Show, Map<Seat, SeatLock>> locks;
    
    public SeatLockProvider(Integer lockTimeOut) {
        this.locks = new ConcurrentHashMap<>();
        this.lockTimeOut = lockTimeOut;
    }

    @Override
    public void lockSeats(Show show, List<Seat> seats, User user) throws Exception {
        Map<Seat, SeatLock> seatLocks = locks.computeIfAbsent(show, s -> new ConcurrentHashMap<>());
        synchronized (seatLocks) {
            for(Seat seat : seats) {
                if(seatLocks.containsKey(seat)) {
                    SeatLock existingLock = seatLocks.get(seat);
                    if(!existingLock.isLockExpired()) {
                        throw new Exception("Seat " + seat.getSeatId() + " is already locked");
                    }
                }
            }
            Date now = new Date();
            for(Seat seat : seats) {
                SeatLock lock = new SeatLock(seat, show, lockTimeOut, now, user);
                seatLocks.put(seat, lock);
            }
        }
    }

    @Override
    public void unlockSeats(Show show, List<Seat> seats, User user) {
        Map<Seat, SeatLock> seatLocks = locks.get(show);
        if(seatLocks == null) return;
        synchronized (seatLocks) {
            for(Seat seat : seats) {
                SeatLock lock = seatLocks.get(seat);
                if(lock != null && lock.getLockedBy().equals(user)) {
                    seatLocks.remove(seat);
                }
            }
        }
    }

    @Override
    public boolean validateLock(Show show, Seat seat, User user) {
        Map<Seat, SeatLock> seatLocks = locks.get(show);
        if(seatLocks == null) return false;
        synchronized (seatLocks) {
            SeatLock lock = seatLocks.get(seat);
            return lock != null && !lock.isLockExpired() && lock.getLockedBy().equals(user);
        }
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        Map<Seat, SeatLock> seatLocks = locks.get(show);
        if(seatLocks == null) {
            return Collections.emptyList();
        }
        synchronized (seatLocks) {
            return seatLocks.entrySet().stream().filter(entry -> !entry.getValue().isLockExpired())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }
}
