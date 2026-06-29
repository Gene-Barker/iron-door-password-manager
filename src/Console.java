import java.util.Scanner;

public class Console {
    Scanner scanner;

    public Console(){
        scanner = new Scanner(System.in);
    }

    public String mainMenu(){
        System.out.println("Sign up, log in, or exit?\n");
        return (scanner.nextLine());
    }

    public String[] inputUsernameAndPassword(){
        String[] usernameAndPassword = new String[2];

        System.out.println("Enter username");
        usernameAndPassword[0] = scanner.nextLine();

        System.out.println("Enter password");
        usernameAndPassword[1] = scanner.nextLine();

        return usernameAndPassword;
    }
//Do i need all of these messages to be separate methods? Prolly not
    public void signUpFailedMessage(){
        System.out.println("Sign up failed");
    }
    public void signUpSuccessfulMessage(){
        System.out.println("Sign up successful");
    }
    public void logInSuccessfulMessage(){
        System.out.println("Log in successful");
    }
    public void logInFailedMessage(){
        System.out.println("Username or password is wrong");
    }

}
