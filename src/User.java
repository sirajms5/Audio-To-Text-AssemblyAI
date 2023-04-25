import java.util.Scanner;

public class User {
    
    private Scanner keyboard = new Scanner(System.in);
    
    // inputString methood will request user to input a string and it will return it
    public String inputString(String message){
        System.out.print(message);
        String userInput = keyboard.nextLine();
        return userInput;
    }
}
