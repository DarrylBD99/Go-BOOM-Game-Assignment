import java.util.*;

import Game.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int type = 0;

        do {
            System.out.print("Please select mode: \n" +
                                "1. GUI (Incomplete, barebones level of incomplete)\n" +
                                "2. Console\n\n" +
                                "Input (-1 to quit) -> ");
            
            try {
                type = input.nextInt();
            } catch (Exception e) {
                System.out.println("\nError: Input is not an integer\n\n");
            }

            if (type == -1) System.exit(0);
            else if (!(type == 1 || type == 2)) System.out.println("\nError: Input is not 1 or 2\n\n");
            else break;
        } while (true);
        
        if (type == 1) new GUI();
        else if (type == 2) new NoGUI();

        input.close();
    }
}
