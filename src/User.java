import java.util.Scanner;

public class User {
    
    private Scanner keyboard = new Scanner(System.in);
    
    public String inputString(String message){
        System.out.print(message);
        String userInput = keyboard.nextLine();
        return userInput;
    }
}
