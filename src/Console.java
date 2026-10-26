import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
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

    public void userNotFoundMessage(){
        System.out.println("User not found, enter another username");
    }

    public String inputUsername(){
        System.out.println("Enter username");
        return(scanner.nextLine());
    }
    public String inputPassword(){
        System.out.println("Enter password");
        return (scanner.nextLine());
    }
    public String inputSitename(){
        System.out.println("Enter site name");
        return (scanner.nextLine());
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

    public String userLoggedInMenu(){
        System.out.println("Do you want to \n1. Get a username and password\n2. Add a username and password\n3. Delete a username and password\n4. Delete this account");
        return (scanner.nextLine());
    }
    public void addAccountSuccessfulMessage(){System.out.println("Added a new account and password");}
    public void addAccountFailedMessage(){System.out.println("Account already exists");}
    public void displayUsernameAndPassword(String[] usernameAndPassword, String siteName){
        if (usernameAndPassword[0] == null){
            System.out.println("No information has been recorded for this site. Please try again");
        }
        else{
            System.out.println("Username and password for " + siteName +"\nUsername: " + usernameAndPassword[0] + "\nPassword: " + usernameAndPassword[1] );
        }
    }

}
