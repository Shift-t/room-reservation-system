package Services;

import entity.User;
import java.io.*;


public class UserRegisterService {

    //instance variable
    private static final String seperator = "||";

    //if a user requests new user registration, this service writes the new user's details in the file
    public static void registerUser(User user){
        if (user.getUser() != null && user.getUser().contains(",")){
            throw new IllegalArgumentException("Username cannot contain commas");
        }
        String[] fields = {
            user.getUser(), user.getPassword(), user.getName(),
            user.getEmail(), user.getPhone(), user.getCategory()
        };
        for (String field : fields) {
            if (field != null && field.contains("||")) {
                throw new IllegalArgumentException("Registration fields cannot contain '||'");
            }
        }
        try(FileOutputStream toFile = new FileOutputStream(DB.path("UserData"), true);
            PrintWriter writer = new PrintWriter(toFile);){

            //creates a string joining all different attributes using the seperator to print to the file
            String userData = String.join(seperator,
            user.getUser(),
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCategory()
            );
            writer.println(userData);
        } catch (IOException e){
            System.out.println("Error reading UserData file" + e);
        }
    }
}
