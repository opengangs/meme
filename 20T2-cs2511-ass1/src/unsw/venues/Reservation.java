package unsw.venues;
import java.time.LocalDate;
import java.util.ArrayList;

public class Reservation {

    // Attributes.
    private String id;                  // ID of reservation.
    private ArrayList<Room> rooms;      // List of rooms related to reservation.
    private LocalDate start;            // Start date of reservation.
    private LocalDate end;              // End date of reservation.

    /**
     * Class constructor.
     * @param id: ID of reservation.
     * @param rooms: List of rooms associated with reservation.
     * @param start: Start date of reservation.
     * @param end: End date of reservation.
     */
    public Reservation (String id, ArrayList<Room> rooms, LocalDate start, LocalDate end) {
        this.id = id;
        this.rooms = rooms;
        this.start = start;
        this.end = end;
    }

    // Getters.
    public String getID() {
        return id;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    // Other methods.
    public void removeRooms() {
        for (Room r : rooms) {
            // remove.
            r.removeReservation (this);
        }
    }

    public void addBackRooms()
 {
     for (Room r : rooms) {
         // add back.
         r.addReservation (this);
     }
 }}