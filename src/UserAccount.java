import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class UserAccount {
    private String userName;
    private String password; //Password is encoded in SHA 265 in this variable
    private boolean loggedIn; //True if the user is logged in. If the user is not logged in the data cannot be accessed

    public UserAccount(){
    }

    public boolean signUp(String username, String password) throws IOException {
        //Returns true if the signup was successful
        boolean signUpSuccessful = true;

        if (!(usernameExists(username))){

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

    public boolean usernameExists(String userName) throws IOException {

        Path filePath = Paths.get(".users",userName);
        boolean usernameExists = false;
        if (Files.exists(filePath)){
            usernameExists = true;
        }

        return usernameExists;
    }

    public void setUsername(String username){
        userName = username;
    }

    public boolean checkPassword (String passwordAttempt) throws IOException {
        Path filePath = Paths.get(".users",userName, userName + ".txt");
        boolean passwordCorrect = false;


        try (Scanner fileReader = new Scanner(filePath)) { //Makes a scanner object with the path to list of users
            while (fileReader.hasNextLine()) {
                if (passwordAttempt.equals(fileReader.nextLine())) {
                    passwordCorrect = true;
                }
            }
        }
        return passwordCorrect;

    }

    public void setLoggedIn(boolean loggedInStatus){
        loggedIn = loggedInStatus;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public String getUserName() {return userName;}

    public String[] readAccount(String siteName) throws IOException {
        //Just return the whole thing and deal with it in main
        String[] userNameAndPassword = new String[4];
        Path filePath = Paths.get(".users", userName, siteName + ".txt");
        if (Files.exists(filePath)){
            try (Scanner fileReader = new Scanner(filePath)) {

                for (int i = 0; fileReader.hasNextLine(); i++){//For loop for going through the file and reading from it
                    String nextLine = fileReader.nextLine();
                    if (!nextLine.isBlank()) {
                        userNameAndPassword[i] = nextLine;
                    }
                    }
            }
        }
        else{//If the account doesnt exist just return null
            userNameAndPassword = new String[] {null, null, null, null};
        }
        return userNameAndPassword;
    }

    public boolean addAccount(String siteName, String accountUsername, String password, String key, String iv) throws IOException {
        boolean accountAdded = false;

        //Account site is the website for the account

        Path filePath = Paths.get(".users", userName, siteName + ".txt");
        //Checking if the file already exists
        if (!(Files.exists(filePath))){
            try (FileWriter fileWriter = new FileWriter(filePath.toFile(), true)){

                fileWriter.write(accountUsername + "\n");
                //Storing the key and password as plain text is pretty stupid lowkey, not secure at all
                //Might fix it one day
                fileWriter.write(password + "\n");
                fileWriter.write(key + "\n");
                fileWriter.write(iv);
                accountAdded = true;
            }
        }
        else {accountAdded = false;}//Techniclly redundent but I like it
        return accountAdded;
    }

    public int deleteAccount (String siteName){
        int exitCode;
        Path filePath = Paths.get(".users", userName, siteName + ".txt");

        File accountFile = new File(filePath.toUri());

        if (accountFile.delete()){
            exitCode = 0;
        }
        else {
            exitCode = 1;
        }
        return exitCode;
    }

    public void deleteThisAccount(){
        Path filePath = Paths.get(".users", userName);

        File accountFolder = new File(filePath.toUri());

        deleteDirectory(accountFolder);
    }

    public void deleteDirectory(File accountFolder){
        File[] userData = accountFolder.listFiles();

        if (userData != null){
            for (File file : userData){
                deleteDirectory(file);
            }
        }
        accountFolder.delete();
    }
}