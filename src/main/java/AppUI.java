import enumsAndConstants.Constants;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class AppUI {

    private final Controller controller = new Controller();

    public void runApplication() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("Proceed by selecting one of the following options:" + "\n" +
                    "1: Get current weather " + "\n" +
                    "2: Get weather forecast for the next 5 days " + "\n" +
                    "3: Get weather from app DB, or " + "\n" +
                    "4: Close the application" + "\n" +
                    "Please, enter number of desired option here:");

            String result = scanner.nextLine();

            try {
                validateUserInput(result);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            checkIsExit(result);

            if (result.equals("1") || result.equals("2")) {
                System.out.println("Please, enter city name:");
                String city = scanner.nextLine();
                setGlobalCity(city);
            }

            try {
                notifyController(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkIsExit(String result) throws IOException, SQLException {
        if (result.equalsIgnoreCase("4")) {
            controller.exitApp();
        }
    }

    private void setGlobalCity(String city) {
        Constants.getInstance().setSelectedCity(city);
    }

    private void validateUserInput(String userInput) throws IOException {
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Warning! One digit should be entered. Your input is: " + userInput);
        }
        int answer;
        try {
            answer = Integer.parseInt(userInput);
            if (answer >= 5){
                throw new IOException("Incorrect user input: character must be in 1-4 range");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Incorrect user input: character isn't a number.");
        }
    }

    private void notifyController(String input) throws IOException, SQLException {
        controller.onUserInput(input);
    }

}
