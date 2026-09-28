import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
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

            if (Objects.equals(userInput, "1")){
                String[] usernameAndPassword = new String[3];
                String username;
                String password;
                String plainKey;
                String plainIv;
                SecretKey key;
                GCMParameterSpec iv;

                String siteName = console.inputSitename();
                usernameAndPassword = currentUser.readAccount(siteName);
                username = usernameAndPassword[0];
                password = usernameAndPassword[1];
                plainKey = usernameAndPassword[2];
                plainIv = usernameAndPassword[3];

                //Convert the key and iv into the correct data type

                // decode the base64 encoded string
                byte[] decodedKey = Base64.getDecoder().decode(plainKey);
                // rebuild key using SecretKeySpec
                key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");


            }

            if (Objects.equals(userInput, "2")) {
                //This code is fucking horrible i hate my life
                String siteName = console.inputSitename();
                GCMParameterSpec iv = generateIv();
                SecretKey key = generateKey();
                String username = console.inputUsername();
                String password = encryptAES("AES/GCM/NoPadding", console.inputPassword(), key, iv);
                if (currentUser.addAccount(siteName, username, password, key, iv)) {
                    console.addAccountSuccessfulMessage();
                } else {
                    console.addAccountFailedMessage();
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


    public static GCMParameterSpec generateIv(){
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(128, iv);
    }
//All of this part is stolen btw, idk where from tho I forgot
public static String encryptAES(String algorithm, String input, SecretKey key, GCMParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    byte[] cipherText = cipher.doFinal(input.getBytes());
    return Base64.getEncoder().encodeToString(cipherText);
}
    public static String decryptAES(String algorithm, String cipherText, SecretKey key, GCMParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
}
