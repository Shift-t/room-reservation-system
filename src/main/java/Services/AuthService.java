package Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class AuthService {
    // instance variables
    private final String file;

    //Constructor
    public AuthService(String filePath) {
        this.file = filePath;
    }

    //returns true if login is valid else returns false
    public boolean login(String username,String password){
        //opens the file given in the constructor and validates the credentials
        try (FileInputStream credsFile = new FileInputStream(file);
             Scanner fileReader = new Scanner(credsFile);){
            fileReader.nextLine();

            while (fileReader.hasNext()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|\\|");
                if (parts[0].equals(username)) {
                    if (parts[1].equals(password)) return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        return false;
    }
}
