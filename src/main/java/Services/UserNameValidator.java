package Services;

import java.io.FileInputStream;
import java.util.Scanner;
import java.io.IOException;

public class UserNameValidator {

    //checks if the provided username matches the criteria and doesn't already exist in the userdata file
    public static boolean isValid(String userName) {
        if (userName.contains(",") || userName.contains("||")) {
            return false;
        }
        if (userName.length() < 1 || userName.length() > 20) {
            return false;
        }

        try (FileInputStream credsFile = new FileInputStream(DB.path("UserData"));
             Scanner fileReader = new Scanner(credsFile);){
            fileReader.nextLine();

            while (fileReader.hasNext()) {
                String line = fileReader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|\\|");
                if (parts[0].equals(userName)) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            System.out.print("Error Readin UserData: " + e);
            return false;
        }
    }
}
