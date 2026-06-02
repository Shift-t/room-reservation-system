package Services;

import entity.Booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GetUserHistoryService {

    //instance variables
    private ArrayList<Booking> userBookings = new ArrayList<>();
    private String username = null;

    //constructor
    public ArrayList<Booking> getHistory(String user) {
        userBookings.clear();
        this.username = user;
        checkPendingRequests();
        checkBookedRooms();
        sorter();
        return userBookings;
    }

    //checks the booked rooms file and looks for rooms booked by the current user
    private void checkBookedRooms(){
        try{
            FileInputStream bookedRoomsFile = new FileInputStream(DB.path("BookedRooms"));
            Scanner fileReader = new Scanner(bookedRoomsFile);
            String line = fileReader.nextLine(); //skis file header

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;
                if (tokens[4].equals(username)) {
                    Booking booking = new Booking(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], "Confirmed");
                    userBookings.add(booking);
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading booked rooms file: " + e.getMessage());
        }
    }

    //checks if the current user has any pending requests and adds it to the array list
    private void checkPendingRequests(){
        try{
            FileInputStream pendingRoomsFile = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRoomsFile);
            String line = fileReader.nextLine();

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 7) continue;
                if (tokens[4].equals(username)) {
                    Booking booking = new Booking(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[6]);
                    userBookings.add(booking);
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading Pending Requests File: " + e.getMessage());
        }
    }

    //sorts the array list to make sure that it is sorted by latest date first
    private void sorter(){
        Booking temp = null;
        for (int i = 0; i < userBookings.size(); i++){
            for (int j = i + 1; j < userBookings.size(); j++){

                LocalDate date1 = LocalDate.parse(userBookings.get(i).getDate());
                LocalDate date2 = LocalDate.parse(userBookings.get(j).getDate());

                if (date1.isBefore(date2)) {
                    temp = userBookings.get(i);
                    userBookings.set(i, userBookings.get(j));
                    userBookings.set(j, temp);
                }
            }
        }
    }
}
