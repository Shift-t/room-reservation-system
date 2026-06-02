package Services;

import entity.Room;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RoomController {

    //Array lists for rooms per floor
    ArrayList<Room> floor1 = new ArrayList<Room>();
    ArrayList<Room> floor2 = new ArrayList<Room>();
    ArrayList<Room> floor3 = new ArrayList<Room>();

    //constructor calls roomsreader so that array lists per floor get ready
    public RoomController() {
        try { roomsReader();}
        catch (IOException e) {System.out.println("roomsReader Error: " + e);}
    }

    //reads through the roomsdata file and adds available rooms to the array list of the respective floors
    public void roomsReader() throws IOException {
        try{
        FileInputStream roomFile = new FileInputStream(DB.path("RoomsData"));
        Scanner roomReader = new Scanner(roomFile);
        String line = roomReader.nextLine();

        floor1.clear();
        floor2.clear();
        floor3.clear();

        while (roomReader.hasNext()) {
            line = roomReader.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.split(",");

            int number = Integer.parseInt(tokens[0]);
            int floor = Integer.parseInt(tokens[1]);
            int capacity = Integer.parseInt(tokens[2]);

            Room room = new Room(number, floor, capacity);

            if (floor == 1) floor1.add(room);
            else if (floor == 2) floor2.add(room);
            else if (floor == 3) floor3.add(room);
        }
        roomReader.close();}
        catch(IOException e) {
            System.out.println("Error Reading Rooms File: " + e.getMessage());
        }
    }

    //getters return requested floor's array list
    public ArrayList<Room> getFloor1(){return this.floor1;}
    public ArrayList<Room> getFloor2(){return this.floor2;}
    public ArrayList<Room> getFloor3(){return this.floor3;}

    //cheks to see if each of the rooms in the array lists are available or booked and sets availability accordingly
    public ArrayList<Room> checkRooms(LocalDate Date, String timeSlot, int floor) throws IOException {

        //defaults all rooms to available
        for (Room room : floor1) room.setAvailability(true);
        for (Room room : floor2) room.setAvailability(true);
        for (Room room : floor3) room.setAvailability(true);

        Set<Integer> bookedRooms = new HashSet<>();
        ArrayList<Room> targetFloor = null;
        try{
            FileInputStream bookedRoomsFile = new FileInputStream(DB.path("BookedRooms"));
            Scanner fileReader = new Scanner(bookedRoomsFile);
            String line = fileReader.nextLine();

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                LocalDate bookingDate = LocalDate.parse(tokens[0]);
                if (bookingDate.isEqual(Date) && timeSlot.equals(tokens[1]) && floor == Integer.parseInt(tokens[2])){
                    bookedRooms.add(Integer.parseInt(tokens[3]));
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading BookedRooms file: " + e.getMessage());
        }

        if (floor == 1) targetFloor = getFloor1();
        else if (floor == 2) targetFloor = getFloor2();
        else if (floor == 3) targetFloor = getFloor3();
        else targetFloor = new ArrayList<Room>();

        for (Room room: targetFloor){
            if (bookedRooms.contains(room.getRoomNumber())){
                room.setAvailability(false);
            }
        }
        return targetFloor;
    }
}
