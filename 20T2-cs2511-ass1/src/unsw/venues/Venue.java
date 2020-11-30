package unsw.venues;
import java.time.LocalDate;
import java.util.ArrayList;

public class Venue {

    // Attributes.
    private String name;            // Name of venue.
    private ArrayList<Room> rooms;  // List of rooms associated with venue.

    /**
     * Class constructor.
     * @param name: Name of venue.
     */
    public Venue (String name) {
        this.name = name;
        rooms = new ArrayList<Room>();
    }

    // Getters.
    public String getName() {
        return name;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    // Other methods.
    /**
     * Method to add a new room to the venue's records.
     * @param room: Name of room.
     * @param size: Size of room.
     */
    public void addRoom (String room, String size) {
        Room rooom = new Room (room, size);
        rooms.add (rooom);
    }

    /**
     * Method to find a list of rooms to be booked.
     * @param s: Start date of booking.
     * @param e: End date of booking.
     * @param small: Number of small rooms requested.
     * @param med: Number of medium rooms requested.
     * @param large: Number of large rooms requested.
     * @return: A list of rooms to be booked. Otherwise, null.
     */
    public ArrayList<Room> listOfRooms (LocalDate s, LocalDate e, int small, int med, int large) {
        ArrayList<Room> ListOfRooms = new ArrayList<Room>();

        // Counters to keep track of how many rooms we're at.
        int noSmalls = 0; int noMediums = 0; int noLarges = 0;

        // Loop through each room in venue.
        for (Room r : rooms) {
            //System.out.println (r.getSize());
            if (r.getSize().equals ("small")) {
                if (r.canBook (s, e) && noSmalls < small) {
                    // then there are still more to iterate.
                    noSmalls = noSmalls + 1;
                    ListOfRooms.add (r);
                } 
            } else if (r.getSize().equals ("medium")) {
                if (r.canBook (s, e) && noMediums < med) {
                    noMediums = noMediums + 1;
                    ListOfRooms.add (r);
                }
            } else if (r.getSize().equals ("large")) {
                if (r.canBook (s, e) && noLarges < large) {
                    noLarges = noLarges + 1;
                    ListOfRooms.add (r);
                }
            }
        }

        // if we met the requirements, then
        // noSmalls = small, noMediums = med, noLarges = large.
        if (noSmalls == small && noMediums == med && noLarges == large) return ListOfRooms;
        return null; // otherwise, failed :(
    }

    // Overriden methods.
    // @Override
    // public boolean equals (Object o) {
    //     if (o == null) return false;
    //     if (getClass() != o.getClass()) return false;

    //     Venue venuee = (Venue) o;
    //     return (this.getName().equals (venuee.getName()));
    // }
}