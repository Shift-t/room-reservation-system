package entity;

public class Room{
    //instance variables
    int roomNumber;
    int roomFloor;
    int roomCapacity;
    private boolean availability;

    //constructor
    public Room(int number, int floor, int capacity) {
        this.roomNumber = number;
        this.roomFloor = floor;
        this.roomCapacity = capacity;
        this.availability = true;
    }

    //getters for different attributes of room
    public Integer getRoomNumber(){
        return roomNumber;
    }
    public Integer getRoomFloor(){
        return roomFloor;
    }
    public Integer getCapacity(){return roomCapacity;}
    public boolean getAvailability(){return availability;}
    public void setAvailability(boolean availability){this.availability = availability;}
}
