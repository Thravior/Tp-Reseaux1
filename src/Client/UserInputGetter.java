package Client;

import java.util.Scanner;

public class UserInputGetter {
    private Scanner userInput = new Scanner(System.in);

    public static String getInitialInput(Scanner input){
            String userInput1;
            do {
                System.out.print("Entrez l'addresse IP du serveur");
                userInput1 = input.nextLine();
            } while (!validateIPAddress(userInput1));

            String userInput2;
            do {
                System.out.print("Entrez le port d'écoute du serveur");
                userInput2 = input.nextLine();
            } while(!validatePort(userInput2));

            return userInput1 + "!" + userInput2;
        }

    public static String getWorkingInput(Scanner input){
        String userInput;
        do{
            System.out.print("Entrez une commande");
            userInput = input.nextLine();
        } while (!validateCommand(userInput));

        return userInput;
    }


        private static boolean validateIPAddress(String input){
            String[] parts = input.split("\\.");
            boolean valide = false;

            if (parts.length == 4){
                for( String part : parts){
                    System.out.println(part);
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
                System.out.print("Format d'addresse IP invalide");
            }

            return valide;
        }

        private static boolean validatePort(String string){
            boolean valide;

            if (!(valide = isNumeral(string))){
                System.out.print("Format de port invalide: pas un nombre \n");
                return false;
            }

            int port = Integer.parseInt(string);
            valide = (port >=5000 && port <= 5050);

            if (!valide){
                System.out.print("Port invalide: Ports disponibles seulement de 5000 a 5050\n");
            }
            return valide;
        }

        private static boolean isNumeral(String string){
            if (string == null || string .equals("")){
                return false;
            }

            try {
                int intValue = Integer.parseInt(string);
                return true;
            } catch(NumberFormatException e) { }
            return false;
        }

        static boolean validateCommand(String string){
            for (String command : commandsRegex){
                if (string.matches(command)){
                    return true;
                }
            }
            System.out.print("Commande invalide");
            return false;
        }

        static String[] commandsRegex = {
                "^ls$",
                "^exit$",
                "^cd\s.+",
                "^mkdir\s.+",
                "^upload\s.+",
                "^download\s.+"
        };


}
