package src;

import java.util.Scanner;


public class UserInfo {
    private String uName;
    private String userID;

    public UserInfo() {
        this.uName = "";
        this.userID = "guest";
    }

    public void readInput(Scanner sc) {
        System.out.print("Enter first name and surname: ");
        this.uName = sc.nextLine();
		while (uName.trim().isEmpty()) { // "   sun sheng   "
		    System.out.println("Error: Name cannot be empty!");
		    System.out.print("Enter first name and surname: ");
		    uName = sc.nextLine();
		}
        this.userID = generateUserID(); 
    }

    public UserInfo(String uName) {
        this.uName = uName;
        this.userID = generateUserID();
    }

    public String getUserName() {
        return uName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserName(String uName) {
        this.uName = uName;
        this.userID = generateUserID();
    }
    
    public boolean isValidName() {
    	return uName != null && uName.trim().split("\\s+").length >= 2; //Lee sun sheng = lee,sun,sheng L
    }

    public String generateUserID() {
        if (!isValidName()) {
            return "guest";
        }
        
        String[] parts = uName.trim().split("\\s+");

        String firstName = parts[0];
        String surname = parts[parts.length - 1];

        return Character.toUpperCase(firstName.charAt(0)) + surname;
    }

    @Override
    public String toString() {
        return "User Name: " + uName + "\nUser ID: " + userID;
    }
}