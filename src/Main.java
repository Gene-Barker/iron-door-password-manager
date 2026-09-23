import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import static java.lang.System.exit;

public class Main{

    static UserAccount currentUser;

    public static void main(String[] args) throws Exception {
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
            usernameAndPassword[1] = encryptSHA(console.inputPassword()); //Password is encrypted as soon as its inputted for extra security I guess


            while (!(currentUser.usernameExists(usernameAndPassword[0]))){
                console.userNotFoundMessage();
                usernameAndPassword[0] = console.inputUsername();
            }
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

        if (userInput.equalsIgnoreCase("exit")){
            exit(0);
        }

        if (currentUser.isLoggedIn()){
            userInput = console.userLoggedInMenu();
            
            if (Objects.equals(userInput, "2")) {
                //This code is fucking horrible i hate my life
                String siteName = console.inputSitename();
                SecretKey key = generateKey();
                String username = console.inputUsername();
                String password = encryptAES(console.inputPassword(), key);
                if (currentUser.addAccount(siteName, username, password, key)){
                    console.addAccountSuccessfulMessage();
                }
            }
        }
    }

    public static String encryptSHA(String unencryptedMessage) throws NoSuchAlgorithmException { //Takes in a string and returns an encoded string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(unencryptedMessage.getBytes(StandardCharsets.UTF_8));


        return (new String(encodedHash));
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }

    public static String encryptAES(String plainText, SecretKey secretKey) throws Exception {
        // 1. Generate a random Initialization Vector (IV) for uniqueness
        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        // 2. Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        // 3. Perform encryption
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // 4. Combine IV and Encrypted Bytes into one array to make management simpler
        byte[] combinedPayload = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedPayload, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedPayload, iv.length, encryptedBytes.length);

        // 5. Encode to Base64 string for safe text transmission
        return Base64.getEncoder().encodeToString(combinedPayload);
    }

}
