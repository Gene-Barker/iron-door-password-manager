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

    public UserAccount(String userName, String password, boolean newAccount) throws IOException {
        if (newAccount){

            System.out.println(usernameAlreadyExists(userName));

            //FileWriter fileWriter = new FileWriter();

        }
    }

    public void signUp(String username, String password) {

    }

    public boolean usernameAlreadyExists(String userName) throws IOException {
        long lines = 0; //Lines in the file. Used to get the length of array of usernames
        Path filePath = Paths.get("users","listOfUsers.txt");
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

}
