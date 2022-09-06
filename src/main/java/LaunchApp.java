import java.io.IOException;
import java.sql.SQLException;


public class LaunchApp {

    public static void main(String[] args) throws SQLException, IOException {
        AppUI userInterface = new AppUI();
        userInterface.runApplication();
    }
}
