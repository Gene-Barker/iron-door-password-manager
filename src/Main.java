import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Main{

    static UserAccount currentUser;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        currentUser = new UserAccount();

        Console console = new Console();

        File usersFolder = new File(".users");
        usersFolder.mkdir(); //Creates a folder for user data if it does not exist

        String userInput = console.mainMenu();

        if (userInput.equalsIgnoreCase("signup")){
            boolean signUpSuccessful = false;
            while (!signUpSuccessful){

                String[] usernameAndPassword = console.inputUsernameAndPassword(); //0 index is username and 1 index is password

                usernameAndPassword[1] = encryptSHA(usernameAndPassword[1]);


                if(currentUser.signUp(usernameAndPassword[0], usernameAndPassword[1])){
                    signUpSuccessful = true;
                    console.signUpSuccessfulMessage();
                }//Stays in a loop until the sign up worked
                else{console.signUpFailedMessage();}
            }
        }
    }

    private static String encryptSHA(String unencryptedMessage) throws NoSuchAlgorithmException { //Takes in a string and returns an encoded string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(unencryptedMessage.getBytes(StandardCharsets.UTF_8));


        return (new String(encodedHash));
    }
}
