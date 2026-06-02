package Services;

import entity.Booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GetRequestHistoryService {
// create arraylist for bookings
    private ArrayList<Booking> allRequests = new ArrayList<>();


    public ArrayList<Booking> getRequests() {
        allRequests.clear();
        checkPendingRequests();
        checkBookedRooms();
        sorter();
        return allRequests;
    }

    private void checkBookedRooms() {
        try {
            // read bookings from file
            FileInputStream bookedRoomsFile = new FileInputStream(DB.path("BookedRooms"));
            Scanner fileReader = new Scanner(bookedRoomsFile);
            String line = fileReader.nextLine(); // Skip header

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;
                // join fields to make booking to add
                Booking booking = new Booking(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], "Confirmed");
                allRequests.add(booking);
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading BookedRooms: " + e.getMessage());
        }
    }

    private void checkPendingRequests() {
        try {
            // reading file
            FileInputStream pendingRoomsFile = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRoomsFile);
            String line = fileReader.nextLine(); // Skip header

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 7) continue;
                Booking booking = new Booking(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[6]);
                allRequests.add(booking);
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading PendingRequests: " + e.getMessage());
        }
    }

    private void sorter() {
        // sorter method
        Booking temp = null;
        for (int i = 0; i < allRequests.size(); i++) {
            for (int j = i + 1; j < allRequests.size(); j++) {
                LocalDate date1 = LocalDate.parse(allRequests.get(i).getDate());
                LocalDate date2 = LocalDate.parse(allRequests.get(j).getDate());

                if (date1.isBefore(date2)) {
                    temp = allRequests.get(i);
                    allRequests.set(i, allRequests.get(j));
                    allRequests.set(j, temp);
                }
            }
        }
    }
}
