package view;

import Services.DB;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.event.EventHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;




public class RemoveRoomScreen implements ScreenInterface{

    static GridPane grid = new GridPane();

    Label title = new Label("Remove a Room");
    Label floorLabel = new Label("Floor: ");
    Label roomLabel = new Label("Room: ");
    Label instruction = new Label("Room must be three digits long and on the right floor");

    TextField roomNumber = new TextField();
    ComboBox<String> floorSelectBox = new ComboBox<>();

    Button addRoomButton = new Button("Remove Room");
    Button goBackButton = new Button("Go Back");


    public RemoveRoomScreen(){
        // set up screen layout
        Insets gridPadding = new Insets(20,20,20,20);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(title,0,0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);

// combo box for floor
        floorSelectBox.getItems().addAll("1", "2", "3");
        floorSelectBox.setPromptText("Choose Floor");
        floorSelectBox.setEditable(false);

        grid.add(floorLabel,0,2);
        grid.add(floorSelectBox,1,2);
        grid.add(roomLabel,0,3);
        grid.add(roomNumber,1,3);
        grid.add(instruction,0,5);
        GridPane.setHalignment(instruction, HPos.CENTER);

        grid.add(addRoomButton,0,6);
        GridPane.setHalignment(addRoomButton, HPos.RIGHT);
        grid.add(goBackButton,1,6);
        GridPane.setHalignment(goBackButton, HPos.LEFT);

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchEditRoomsScreen();
            }
        });

        addRoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // validation
                if (validate(floorSelectBox.getValue(), roomNumber.getText())) {
                    if (!checkExistence(roomNumber.getText())){
                        removeRoom(roomNumber.getText());
                        instruction.setText("Room Removed!");
                        instruction.setStyle("-fx-text-fill: green;");
                        addRoomButton.setDisable(true);

                    }
                    else{
                        instruction.setText("Room does not exist!");
                        instruction.setStyle("-fx-text-fill: red;");
                    }
                }
                else{
                    instruction.setText("Room must be three digits long and on the right floor");
                    instruction.setStyle("-fx-text-fill: red;");
                }
            }
        });


    }
// check if room entered is valid and on the right floor
    public boolean validate(String floor, String room){
        if (floor == null || floor.trim().isEmpty()) {
            return false;
        }  

        try {
            int selectedFloor = Integer.parseInt(floor);
            int selectedRoom = Integer.parseInt(room);

            if (selectedFloor < 1 || selectedFloor > 3) {
                return false;
            }

            if (selectedRoom < 100 || selectedRoom > 999) {
                return false;
            }

            int floorFromRoomNumber = selectedRoom / 100;
            if (floorFromRoomNumber != selectedFloor) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
// check if room exists
    public boolean checkExistence(String room){
        try {
            FileInputStream roomsFile = new FileInputStream(DB.path("RoomsData"));
            Scanner fileReader = new Scanner(roomsFile);
            String line =  fileReader.nextLine();

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens[0].equals(room)) {
                    return false;
                }
            }
            fileReader.close();

            return true;


        } catch (IOException e) {
            return false;
        }

    }
// delete room from file
    public void removeRoom(String room){

        try {
            // read all lines into temp EXCEPT the room chosen
            FileInputStream roomsFile = new FileInputStream(DB.path("RoomsData"));
            Scanner fileReader = new Scanner(roomsFile);
            String line =  fileReader.nextLine();
            String temp = line + "\n";

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (!tokens[0].equals(room)) {
                    // do not add room to be deleted
                    temp = temp + line + "\n";
                }
            }
            fileReader.close();

            // clear file and write updated rooms
            FileOutputStream toFile = new FileOutputStream(DB.path("RoomsData"), false);
            PrintWriter fileWriter = new PrintWriter(toFile);
            fileWriter.print(temp);
            fileWriter.close();

        }catch (IOException e){
            System.out.println(e);
        }

    }

    public void dispose(){
        instruction.setText("");
        grid.getChildren().clear();
    }

    public GridPane getView(){
        return grid;
    }

}
