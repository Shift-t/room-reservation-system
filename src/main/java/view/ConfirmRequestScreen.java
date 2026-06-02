package view;

import Services.GetUserDataService;
import Services.RequestRoomService;
import entity.Room;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;

public class ConfirmRequestScreen implements ScreenInterface{

    // initializing instance variables
    private final GridPane grid = new GridPane();
    private final Label statusLabel = new Label("Please Confirm the Information");

    private final Button backButton = new Button("< Previous Page");
    private final Button confirmButton = new Button("Confirm & Submit Request");

    private final String bookingUser;
    private final Room bookingRoom;
    private final LocalDate requestedDate;
    private final String requestedTimeSlot;
    private final String bookingReason;

    //constructor
    public ConfirmRequestScreen(String username, Room room, LocalDate date,
                                String timeSlot, String reason) {
        this.bookingUser = username;
        this.bookingRoom = room;
        this.requestedDate = date;
        this.requestedTimeSlot = timeSlot;
        this.bookingReason = reason;

        setupGrid();
        setupButtons();
    }

    //sets up the main grid for this screen
    private void setupGrid(){
        Insets gridPadding = new Insets(10, 10, 10, 10);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);


        Label header = new Label("Request Confirmation");
        Label usernameLabel = new Label("Username: ");
        Label nameLabel = new Label("Name: ");
        Label emailLabel = new Label("Email: ");
        Label categoryLabel = new Label("Category: ");
        Label roomLabel = new Label("Room: ");
        Label floorLabel = new Label("Floor: ");
        Label dateLabel = new Label("Date: ");
        Label timeSlotLabel = new Label("Time Slot: ");
        Label reasonLabel = new Label("Reason: ");

        //Gets the users data by taking the username as an argument
        GetUserDataService userData = new GetUserDataService(bookingUser);

        Label userValLabel = new Label(bookingUser);
        Label nameValLabel = new Label(userData.getName());
        Label emailValLabel = new Label(userData.getEmail());
        Label categoryValLabel = new Label(userData.getCategory());
        Label roomValLabel = new Label(bookingRoom.getRoomNumber() + " (Capacity: " + bookingRoom.getCapacity() + ")");
        Label floorValLabel = new Label("" + bookingRoom.getRoomFloor());
        Label dateValLabel = new Label("" + requestedDate);
        Label timeSlotValLabel = new Label(requestedTimeSlot);
        Label reasonValLabel = new Label(bookingReason);


        grid.add(header, 0, 0, 6, 1);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(usernameLabel, 0, 2);
        grid.add(userValLabel, 1, 2);
        grid.add(roomLabel, 4, 2);
        grid.add(roomValLabel, 5, 2);
        grid.add(nameLabel, 0, 3);
        grid.add(nameValLabel, 1, 3);
        grid.add(floorLabel, 4, 3);
        grid.add(floorValLabel, 5, 3);
        grid.add(emailLabel, 0, 4);
        grid.add(emailValLabel, 1, 4);
        grid.add(dateLabel, 4, 4);
        grid.add(dateValLabel, 5, 4);
        grid.add(categoryLabel, 0, 5);
        grid.add(categoryValLabel, 1, 5);
        grid.add(timeSlotLabel, 4, 5);
        grid.add(timeSlotValLabel, 5, 5);
        grid.add(reasonLabel, 0, 6);
        grid.add(reasonValLabel, 1, 6,5,1);
        grid.add(statusLabel, 0, 8,6,1);
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        statusLabel.setStyle("");
        grid.add(backButton, 0, 10,3,1);
        GridPane.setHalignment(backButton, HPos.LEFT);
        grid.add(confirmButton, 4, 10,3,1);
        GridPane.setHalignment(confirmButton, HPos.RIGHT);

    }

    //sets up the buttons of this screen and adds event handlers for them
    private void setupButtons(){
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchUserDashboardScreen(bookingUser);
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    RequestRoomService.requestRoom(requestedDate, requestedTimeSlot, bookingRoom.getRoomFloor(), bookingRoom.getRoomNumber(), bookingUser, bookingReason);
                    confirmButton.setDisable(true);
                    statusLabel.setText("Your Request Has Been Submitted Successfully");
                    statusLabel.setStyle("-fx-text-fill: green");
                    backButton.setText("< Return to User Dashboard");
                } catch (IllegalArgumentException e) {
                    statusLabel.setText(e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red");
                }
            }
        });

    }

    // returns the grid of this screem
    @Override
    public GridPane getView(){
        return grid;
    }

    //helps clear all elements of this screen before switching away from this screen
    @Override
    public void dispose(){
        backButton.setOnAction(null);
        confirmButton.setOnAction(null);

        confirmButton.setDisable(false);
        statusLabel.setText("Please Confirm the Information");
        statusLabel.setStyle("");
        backButton.setText("< Previous Page");

        grid.getChildren().clear();
    }
}
