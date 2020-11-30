package unsw.venues;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

//import jdk.vm.ci.meta.Local;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Gerald Huang
 *
 */
public class VenueHireSystem {

    ArrayList<Venue> venues;                // list of venues in the system.
    ArrayList<Reservation> reservations;    // list of reservations in the system.
    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
        // TODO Auto-generated constructor stub
        this.venues = new ArrayList<Venue>(); // create a new arraylist.
        reservations = new ArrayList<Reservation>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        // TODO Implement other commands

        case "change":
            String idC = json.getString("id");
            LocalDate startC = LocalDate.parse(json.getString("start"));
            LocalDate endC = LocalDate.parse(json.getString("end"));
            int smallC = json.getInt("small");
            int mediumC = json.getInt("medium");
            int largeC = json.getInt("large");

            JSONObject resultC = change(idC, startC, endC, smallC, mediumC, largeC);

            System.out.println(resultC.toString(2));
            break;
        
        case "cancel":
            String idCa = json.getString ("id");
            cancel (idCa);
            break;

        case "list":
            String listV = json.getString ("venue");
            JSONArray finalList = list (listV);
            System.out.println (finalList.toString(2));
            break;
        }
    }

    private void addRoom (String venue, String room, String size) {
        // TODO Process the room command
        Venue finalV = null;
        for (Venue v : venues) {
            if (v.getName().equals (venue)) {
                finalV = v;
                break; // found venue.
            }
        }

        if (finalV == null) {
            // add new venue.
            finalV = new Venue (venue);
            venues.add (finalV);
        }

        finalV.addRoom (room, size);
        //System.out.println ("Added room " + room + " into Venue " + finalV.getName());
    }

    private JSONObject request(String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();

        // TODO Process the request commmand
        // 1. Check each venue for a room that fulfills request.
        // If the request cannot be fulfilled, then status = rejected.
        // Otherwise, make a booking and add it to the system.

        // Finally return the JSONObject.

        ArrayList<Room> listofRooms = null;
        Venue finalV = null;
        for (Venue v : venues) {
            listofRooms = v.listOfRooms(start, end, small, medium, large);
            if (listofRooms != null) {
                finalV = v; break;
            }
        }

        // If there are no rooms available, reject.
        if (listofRooms == null) {
            // reject.
            return rejectRequest (result);
        }

        // Otherwise, make a reservation.
        Reservation r = new Reservation (id, listofRooms, start, end);
        reservations.add (r);

        // Store reservation in each room.
        for (Room reee : listofRooms) {
            reee.addReservation (r);
        }

        // accept request.
        return acceptRequest (result, finalV, listofRooms);
    }

    private JSONObject rejectRequest (JSONObject request) {
        request.put ("status", "rejected");
        return request;
    }

    private JSONObject acceptRequest (JSONObject request, Venue venue, ArrayList<Room> room) {
        request.put ("status", "success");
        request.put ("venue", venue.getName());

        JSONArray rooms = new JSONArray();

        for (Room r : room) {
            rooms.put (r.getName());
        }

        request.put ("rooms", rooms);
        return request;
    }

    private JSONObject change (String id, LocalDate start, LocalDate end, int small, int medium, int large) {
        JSONObject result = new JSONObject();

        // Find the old reservation.
        Reservation original = null;
        for (Reservation r : reservations) {
            if (r.getID().equals (id)) {
                original = r; break;
            }
        }

        reservations.remove (original);
        // remove rooms.
        original.removeRooms();

        // Add a new reservation.
        ArrayList<Room> roooom = null;
        Venue finalV = null;
        for (Venue v : venues) {
            roooom = v.listOfRooms(start, end, small, medium, large);
            //System.out.println (roooom);
            if (roooom != null) {
                finalV = v; break;
            }
        }

        if (roooom == null) {
            // reject the new request and bring back the old reservation.
            reservations.add (original);
            original.addBackRooms();

            // reject.
            return rejectRequest (result);
        }

        // Otherwise, there's a successful change.
        Reservation r = new Reservation (id, roooom, start, end);
        reservations.add (r);

        for (Room eachRoom : roooom) {
            eachRoom.addReservation (r);
        }


        return acceptRequest (result, finalV, roooom);
    }

    private void cancel (String id) {
        
        for (Reservation r : reservations) {
            if (r.getID().equals (id)) {
                r.removeRooms();
                reservations.remove (r);
                break;
            }
        }
    }

    public Venue getVenue (String v) {
        for (Venue venue : venues) {
            if (venue.getName().equals (v)) return venue;
        }

        return null;
    }

    public JSONArray list (String venue) {
        JSONArray result = new JSONArray();

        Venue v = getVenue (venue);
        ArrayList<Room> rooms = v.getRooms();

        for (Room r : rooms) {
            JSONObject o = new JSONObject();
            o.put ("room", r.getName());
            JSONArray output = r.listOutput();
            o.put ("reservations", output);

            result.put (o);
        }
        return result;
        
    }

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
