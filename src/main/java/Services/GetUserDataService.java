package Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class GetUserDataService {

    //instance variables
    String name;
    String email;
    String category;

    //Reads the userdata file to get the details for the said user
    public GetUserDataService(String username) {

        try{
            FileInputStream userData = new FileInputStream(DB.path("UserData"));
            Scanner fileReader = new Scanner(userData);
            String line = fileReader.nextLine();

            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split("\\|\\|");
                if (tokens[0].equals(username)) {
                    this.name = tokens[2];
                    this.email = tokens[3];
                    this.category = (tokens.length > 5) ? tokens[5] : tokens[4];
                }
            }
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading UserData file: " + e.getMessage());
        }
    }

    //returns the name of the user
    public String getName(){
        return name;
    }

    //returns the email of the user
    public String getEmail(){
        return email;
    }

    //returns the category of the user
    public String getCategory(){
        return category;
    }
}
