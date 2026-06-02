package entity;

import java.time.LocalDate;

public class Booking {
    //instance variables
    String date;
    String timeSlot;
    String floor;
    String room;
    String username;
    String reason;
    String status;

    //constructor with reason argument
    public Booking(String date, String timeSlot, String floor, String room, String username, String reason, String status) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.floor = floor;
        this.room = room;
        this.username = username;
        this.reason = reason;
        this.status = status;
    }

    //constructor without reason argument
    public Booking(String date, String timeSlot, String floor, String room, String username, String status) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.floor = floor;
        this.room = room;
        this.username = username;
        this.status = status;
    }

    //getters to get the values of requested variables
    public String getDate() {
        return date;
    }
    public String getTimeSlot() {
        return timeSlot;
    }
    public String getFloor() {
        return floor;
    }
    public String getRoom() {
        return room;
    }
    public String getUsername() {
        return username;
    }
    public String getReason() {
        return reason;
    }
    public String getStatus() {
        return status;
    }

    //converts the shortcode of the status to readable output for user
    public String getWordStatus(){
        if (status.equalsIgnoreCase("P")){
            return "Pending";
        }
        else if (status.equalsIgnoreCase("R")){
            return "Rejected";
        }
        else if (status.equalsIgnoreCase("Confirmed")){
            if (LocalDate.now().isAfter(LocalDate.parse(date))){
                return "Expired";
            }
        }
        return status;
    }

}
