package server;

import java.util.Scanner;

public class UserInputGetter {
    private Scanner userInput = new Scanner(System.in);

    public static String getInitialInput(Scanner input){
            String userInput1;
            do {
                System.out.print("Enter server IP address\n");
                userInput1 = input.nextLine();
            } while (!validateIPAddress(userInput1));

            String userInput2;
            do {
                System.out.print("Enter serveur port\n");
                userInput2 = input.nextLine();
            } while(!validatePort(userInput2));

            return userInput1 + "!" + userInput2;
        }

        private static boolean validateIPAddress(String input){
            String[] parts = input.split("\\.");
            boolean valide = false;

            if (parts.length == 4){
                for( String part : parts){
                    if (!( valide = isNumeral(part)) ) {
                        break;
                    }
                    int value = Integer.parseInt(part);
                    if (value < 0 || value > 255) {
                        valide = false;
                    }
                }
            }

            if (!valide){
                System.out.print("Invalid IP Format\n");
            }

            return valide;
        }

        private static boolean validatePort(String string){
            boolean valide;

            if (!(valide = isNumeral(string))){
                System.out.print("Invalid port Format: not a number\n");
                return false;
            }

            int port = Integer.parseInt(string);
            valide = (port >=5000 && port <= 5050);

            if (!valide){
                System.out.print("Invalid port: only available between 5000 and 5050\n");
            }
            return valide;
        }

        private static boolean isNumeral(String string){
            if (string == null || string .equals("")){
                return false;
            }

            try {
                Integer.parseInt(string);
                return true;
            } catch(NumberFormatException e) { }
            return false;
        }
}
