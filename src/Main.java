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

                String[] usernameAndPassword = new String[2];
                //0 index is username and 1 index is password

                usernameAndPassword[0] = console.inputUsername();

                usernameAndPassword[1] = encryptSHA(console.inputPassword());


                if(currentUser.signUp(usernameAndPassword[0], usernameAndPassword[1])){
                    signUpSuccessful = true;
                    console.signUpSuccessfulMessage();
                }//Stays in a loop until the signup worked
                else{console.signUpFailedMessage();}
            }
        }

        if (userInput.equalsIgnoreCase("login")){
            String[] usernameAndPassword = new String[2];

            usernameAndPassword[0] = console.inputUsername();
            usernameAndPassword[1] = encryptSHA(console.inputPassword());

            System.out.println("1");

            while (!(currentUser.usernameExists(usernameAndPassword[0]))){
                System.out.println("2");
                console.userNotFoundMessage();
                usernameAndPassword[0] = console.inputUsername();
            }
            System.out.println("3");
            currentUser.setUsername(usernameAndPassword[0]);

            for (int i = 0; i < 3; i++){ //Only has 3 attempts allowed

                if (currentUser.checkPassword(usernameAndPassword[1])){
                    console.logInSuccessfulMessage();
                    currentUser.setLoggedIn(true);
                    break;
                }
                else{
                    console.logInFailedMessage();
                    usernameAndPassword[0] = console.inputUsername();
                    usernameAndPassword[1] = encryptSHA(console.inputPassword());
                }
            }
        }
    }

    private static String encryptSHA(String unencryptedMessage) throws NoSuchAlgorithmException { //Takes in a string and returns an encoded string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(unencryptedMessage.getBytes(StandardCharsets.UTF_8));


        return (new String(encodedHash));
    }
}
