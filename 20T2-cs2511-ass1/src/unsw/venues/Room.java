package unsw.venues;
import java.time.LocalDate;
import java.util.ArrayList;

// Used in sorting dates.
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONObject;
import org.json.JSONArray;

public class Room {

    // Attributes.
    private String name;                            // Name of room.
    private String size;                            // Size of room.
    private ArrayList<Reservation> reservations;    // List of reservations associated with room.

    public Room (String name, String size) {
        this.name = name;
        this.size = size;
        reservations = new ArrayList<Reservation>();
    }

    // Getters.
    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public ArrayList<Reservation> getReservations() {
        return  reservations;
    }

    // Other methods.
    /**
     * Method to add a reservation into room's records.
     * @param reserve: New reservation.
     */
    public void addReservation (Reservation reserve) {
        reservations.add (reserve);
    }

    /**
     * Method to delete a reservation from room's records.
     * @param reserve: Reservation to remove.
     */
    public void removeReservation (Reservation reserve) {
        reservations.remove (reserve);
    }

    /**
     * Given a duration of time, see if we can book the room.
     * The room can only be booked if, for every reservation in the room,
     * the start date of all the current reservations is after the end of the new AND
     * the end date of all current reservations is before the start of the new reservation.
     * @param s: start date of new reservation.
     * @param e: end date of the new reservation
     * @return: true if can be booked. Otherwise, false.
     */
    public boolean canBook (LocalDate s, LocalDate e) {
        for (Reservation r : reservations) {
            if (!(r.getEnd().isBefore (s) || r.getStart().isAfter (e))) return false;
        }
        return true;
    }

    /**
     * Sorts the reservation by date ready for printing.
     * Code inspired from Stack Overflow.
     * https://stackoverflow.com/a/5927408
     */
    public void sortByDate() {
        // Sort by date of reservation ready for printing.
        Collections.sort (reservations, new Comparator<Reservation>() {
            public int compare (Reservation r1, Reservation r2) {
                return r1.getStart().compareTo (r2.getStart());
            }
        });
    }

    /**
     * Method to place JSONObjects in order of dates.
     * @return: Returns a JSONArray ready for list.
     */
    public JSONArray listOutput() {
        sortByDate(); // sort the dates.

        // print.
        JSONArray Res = new JSONArray();
        for (Reservation r : reservations) {

            JSONObject reservationObject = new JSONObject();
            reservationObject.put ("id", r.getID());
            reservationObject.put ("start", r.getStart().toString());
            reservationObject.put ("end", r.getEnd().toString());
            Res.put (reservationObject);
        }

        return Res;
    }

    // Overriden methods.
    public boolean equals (Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        Room room = (Room) o;
        return (this.getName().equals (room.getName()));
    }
}