package view;

import Services.DB;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;



public class ActiveRequestsScreen implements ScreenInterface{

    static GridPane grid = new GridPane();

    Label header = new Label("Active Requests");
    Label usernameLabel = new Label("Username");
    Label dateLabel = new Label("Date");
    Label floorLabel = new Label("Floor");
    Label timeSlotLabel = new Label("Time Slot");
    Label reasonLabel = new Label("Reason");
    Label roomLabel = new Label("Room");
    Label statusLabel = new Label("Decision");

    Button previousPage = new Button("Previous Page");
    Button saveChanges = new Button("Save Changes");

    int currentRow = 5;



    public ActiveRequestsScreen() {

        try{
            FileInputStream pendingRequests = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRequests);
            String line = fileReader.nextLine();

            Insets gridPadding = new Insets(20,20,20,20);
            grid.setPadding(gridPadding);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setAlignment(Pos.CENTER);

            grid.add(header, 3, 0);
            grid.add(usernameLabel, 4, 3);
            grid.add(dateLabel, 0, 3);
            grid.add(floorLabel, 2, 3);
            grid.add(timeSlotLabel, 1, 3);
            grid.add(reasonLabel, 5, 3);
            grid.add(roomLabel, 3, 3);
            grid.add(statusLabel, 6, 3);

            while (fileReader.hasNext()){

                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");

                if (tokens[6].equals("P")){
                    Label currentDate = new Label(tokens[0]);
                    Label currentTimeSlot = new Label(tokens[1]);
                    Label currentFloor = new Label(tokens[2]);
                    Label currentRoom = new Label(tokens[3]);
                    Label currentUser = new Label(tokens[4]);
                    Label currentReason = new Label(tokens[5]);
                    Label seperator = new Label("");

                    Button approveButton = new Button("Approve");
                    Button rejectButton = new Button("Reject");
                    approveButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    rejectButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                    approveButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            approveRequest(tokens[0], tokens[1],  tokens[2], tokens[3], tokens[4], tokens[5]);
                        }
                    });

                    rejectButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            rejectRequest(tokens[0], tokens[1],  tokens[2], tokens[3], tokens[4], tokens[5]);
                        }
                    });


                    grid.add(currentDate, 0, currentRow);
                    grid.add(currentTimeSlot, 1, currentRow);
                    grid.add(currentFloor, 2, currentRow);
                    grid.add(currentRoom, 3, currentRow);
                    grid.add(currentUser, 4, currentRow);
                    grid.add(currentReason, 5, currentRow);
                    grid.add(approveButton, 6, currentRow);
                    grid.add(rejectButton, 7, currentRow);
                    grid.add(seperator, 0, ++currentRow);

                }



                currentRow++;

            }
            grid.add(previousPage, 0, currentRow);
            previousPage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ScreenController.launchAdminDashboardScreen();
                }
            });


            fileReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void approveRequest(String date, String timeSlot, String floor, String room, String username, String reason) {

        try {
            // Check if already booked
            boolean alreadyBooked = false;
            try {
                FileInputStream bookedRoomsFile = new FileInputStream(DB.path("BookedRooms"));
                Scanner bookedReader = new Scanner(bookedRoomsFile);
                if (bookedReader.hasNextLine()) bookedReader.nextLine(); // skip header

                while (bookedReader.hasNextLine()) {
                    String bLine = bookedReader.nextLine();
                    if (bLine.trim().isEmpty()) continue;
                    String[] bTokens = bLine.split(",");
                    if (bTokens.length > 3 && bTokens[0].equals(date) && bTokens[1].equals(timeSlot) && bTokens[2].equals(floor) && bTokens[3].equals(room)) {
                        alreadyBooked = true;
                        break;
                    }
                }
                bookedReader.close();
            } catch (IOException e) {
                // If file doesn't exist or error, assume not booked
            }

            if (alreadyBooked) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Room is already booked for this slot!");
                alert.showAndWait();
                return;
            }

            FileInputStream pendingRequests = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRequests);

            String seperator = ",";
            String oldLine = String.join(seperator, date, timeSlot, floor, room, username, reason, "P");
            String newLine = String.join(seperator, date, timeSlot, floor, room, username);
            String temp = "";

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;

                if (line.equals(oldLine)) {
                    continue; // Remove the approved request from pending
                }

                String[] tokens = line.split(seperator);
                if (tokens.length > 6 && tokens[0].equals(date) && tokens[1].equals(timeSlot) && tokens[2].equals(floor) && tokens[3].equals(room) && tokens[6].equals("P")) {
                    // Conflicting pending request: reject
                    tokens[6] = "R";
                    temp = temp + String.join(seperator, tokens) + "\n";
                } else {
                    temp = temp + line + "\n";
                }
            }

            fileReader.close();

            // rewrite file without old line and with updated (rejected) conflicting lines
            FileOutputStream pendingFile = new FileOutputStream(DB.path("PendingRequests"), false);
            PrintWriter pendingWriter = new PrintWriter(pendingFile);
            pendingWriter.print(temp);
            pendingWriter.close();

            // add line to BookedRooms
            FileOutputStream bookedFile = new FileOutputStream(DB.path("BookedRooms"), true);
            PrintWriter bookedWriter = new PrintWriter(bookedFile);
            bookedWriter.println(newLine);
            bookedWriter.close();

            // refresh page
            ScreenController.launchActiveRequestsScreen();

        } catch (IOException e){
            System.out.println(e);
        }

    }

    public void rejectRequest(String date, String timeSlot, String floor, String room, String username, String reason){

        try {
            FileInputStream pendingRequests = new FileInputStream(DB.path("PendingRequests"));
            Scanner fileReader = new Scanner(pendingRequests);

            String seperator = ",";
            String oldLine = String.join(seperator, date, timeSlot, floor, room, username, reason, "P");
            String newLine = String.join(seperator, date, timeSlot, floor, room, username, reason, "R");
            String temp = "";

            while (fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;

                if (line.equals(oldLine)){
                    temp = temp + newLine + "\n";
                }
                else{
                    temp = temp + line + "\n";
                }
            }

            fileReader.close();

            // rewrite file with updated line
            FileOutputStream toFile = new FileOutputStream(DB.path("PendingRequests"), false);
            PrintWriter fileWriter = new PrintWriter(toFile);
            fileWriter.print(temp);
            fileWriter.close();

            ScreenController.launchActiveRequestsScreen(); //refresh the page



        } catch (IOException e){
            System.out.println(e);
        }

    }


    public GridPane getView(){
        return grid;
    }

    public void dispose(){
        previousPage.setOnAction(null);
        saveChanges.setOnAction(null);
        grid.getChildren().clear();
    }

}
