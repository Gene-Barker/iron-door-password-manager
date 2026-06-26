import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Main{
    public static void main(String[] args) throws NoSuchAlgorithmException {

        File userFolder = new File("users");
        userFolder.mkdir();


        Main main = new Main();

    }

    public String encrypt(String unencryptedMessage) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(unencryptedMessage.getBytes(StandardCharsets.UTF_8));


        return (new String(encodedHash));
    }
}
