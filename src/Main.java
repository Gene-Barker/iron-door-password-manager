import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Main{

    static UserAccount currentUser;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        Console console = new Console();

        File usersFolder = new File("users");
        usersFolder.mkdir(); //Creates a folder for user data if it does not exist

        String userInput = console.mainMenu();

        if (userInput.equalsIgnoreCase("signup")){


            String[] usernameAndPassword = console.inputUsernameAndPassword(); //0 index is username and 1 index is password

            usernameAndPassword[1] = encrypt(usernameAndPassword[1]);

            currentUser = new UserAccount();
            currentUser.signUp(usernameAndPassword[0], usernameAndPassword[1]);
        }
    }

    private static String encrypt(String unencryptedMessage) throws NoSuchAlgorithmException { //Takes in a string and returns an encoded string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(unencryptedMessage.getBytes(StandardCharsets.UTF_8));


        return (new String(encodedHash));
    }
}
