import enumsAndConstants.AppOptions;
import enumsAndConstants.ForecastPeriod;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    AccuWeather provider = new AccuWeather();
    SQLite repository = new SQLite();

    Map<Integer, AppOptions> variantResult = new HashMap();

    public Controller() {
        variantResult.put(1, AppOptions.GET_CURRENT);
        variantResult.put(2, AppOptions.GET_5_DAYS);
        variantResult.put(3, AppOptions.GET_DB);
        variantResult.put(4, AppOptions.EXIT);
    }

    public void onUserInput(String input) throws IOException, SQLException {
        int command = Integer.parseInt(input);
        if (!variantResult.containsKey(command)) {
            throw new IOException("There is no command for command-key " + command);
        }

        switch (variantResult.get(command)) {
            case GET_CURRENT:
                getCurrentWeather();
                break;
            case GET_5_DAYS:
                getWeatherIn5Days();
                break;
            case GET_DB:
                getWeatherFromDB();
                break;
            case EXIT:
                exitApp();
                break;
        }
    }

    public void getCurrentWeather() throws IOException, SQLException {
        provider.getWeather(ForecastPeriod.NOW);
    }

    public void getWeatherIn5Days() throws IOException, SQLException {
        provider.getWeather(ForecastPeriod.FIVE_DAYS);
    }

    public void getWeatherFromDB() throws IOException, SQLException {
        provider.getWeather(ForecastPeriod.BASE);
    }

    public void exitApp() throws IOException, SQLException {
        System.out.println("Application closed");
        repository.closeConnection();
        System.exit(4);
    }
}
