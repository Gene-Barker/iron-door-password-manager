import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class UserAccount {
    String userName;
    String password; //Password is encoded in SHA 265 in this variable
    boolean loggedIn; //True if the user is logged in. If the user is not logged in the data cannot be accessed

    public UserAccount(){
    }

    public boolean signUp(String username, String password) throws IOException {
        //Returns true if the signup was successful
        boolean signUpSuccessful = true;

        if (!(usernameAlreadyExists(username))){
            Path userListFile = Paths.get(".users", "listOfUsers.txt");
            try (FileWriter fileWriter = new FileWriter(userListFile.toFile(), true)){
                fileWriter.write(username + "\n");
            }

            loggedIn = true;
            Path userFolder = Paths.get(".users", username); //Folder to hold user data
            Path userFile = Paths.get(".users", username, username + ".txt"); //Text file which has the username and encoded password
            Files.createDirectory(userFolder);
            Files.createFile(userFile);
            try (FileWriter fileWriter = new FileWriter(userFile.toFile())){
                //Writes the username and password to file, in that order
                fileWriter.write(username + "\n");
                fileWriter.write(password + "\n");
            }

        } else{signUpSuccessful = false;}

        return (signUpSuccessful);
    }

    public boolean usernameAlreadyExists(String userName) throws IOException {
        long lines = 0; //Lines in the file. Used to get the length of array of usernames
        Path filePath = Paths.get(".users","listOfUsers.txt");
        boolean usernameExists = false;

        if (!(Files.exists(filePath))){Files.createFile(filePath);} //Can't do this in the except block, I'm sure someone else can figure that out

        try {
            lines = Files.lines(filePath).count(); //Method gets number of lines in the file
            int linesInt = Math.toIntExact(lines); //Have to convert to int

            try (Scanner fileReader = new Scanner(filePath)) { //Makes a scanner object with the path to list of users
                for (int i = 0; fileReader.hasNextLine(); i++){
                     if (userName.equals(fileReader.nextLine())){usernameExists = true;}
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }


        return usernameExists;
    }

    public boolean logIn(String userNameAttempt, String passwordAttempt){

    }

}
