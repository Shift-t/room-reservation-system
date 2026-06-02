package entity;

public class User{
    //instance variables
    String user;
    String email;
    String password;
    String phone;
    String name;
    String category;

    //constructor
    public User(String username, String password, String name, String email, String phoneNumber, String Category) {
        this.user = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phoneNumber;
        this.category = Category;
    }

    //getters for differennt user attributes
    public String getUser(){
        return user;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getCategory(){
        return category;
    }
}
