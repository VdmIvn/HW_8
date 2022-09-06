import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import enumsAndConstants.Constants;
import enumsAndConstants.ForecastPeriod;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccuWeather {

    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";
    private static final String API_KEY = Constants.getInstance().getApiKey();
    private static final String FORECAST_PERIOD = "5day";
    private static final String FORECAST_TYPE = "daily";
    private static final String FORECAST = "forecasts";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SQLite repository = new SQLite();

    public void getWeather(ForecastPeriod periods) throws IOException, SQLException {

        if (periods.equals(ForecastPeriod.NOW)) {
            String cityKey = detectCityKey();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "en-en")
                    .addQueryParameter("metric", "true")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            String responseList = client.newCall(request).execute().body().string();

            List<WeatherResponse> weatherResponseList = objectMapper.readValue(responseList, new TypeReference<List<WeatherResponse>>() {});

            WeatherResponse weather = weatherResponseList.get(0);

            System.out.println(Constants.getInstance().getSelectedCity() +
                    ": "  + weather.getTemperature().getMetric().getValue() +
                    "°С, " + weather.getWeatherText() + ".");
        }

        if (periods.equals(ForecastPeriod.FIVE_DAYS)) {
            String cityKey = detectCityKey();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECAST)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(FORECAST_TYPE)
                    .addPathSegment(FORECAST_PERIOD)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "en-en")
                    .addQueryParameter("metric", "true")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            String responseList = client.newCall(request).execute().body().string();

            int firstIndexBody = responseList.indexOf("[{\"Date\"");
            int lastIndexBody = responseList.lastIndexOf("}");
            responseList = responseList.substring(firstIndexBody, lastIndexBody);

            List<WeatherResponse> weatherResponseList = objectMapper.readValue(responseList, new TypeReference<List<WeatherResponse>>() {});

            for (WeatherResponse weather: weatherResponseList) {
                System.out.println("For the next day " + weather.getDate().substring(0,10) +
                        " min "  + weather.getTemperature().getMinimum().getValue() +
                        "°С. max  " + weather.getTemperature().getMaximum().getValue() +
                        "°С. Day - " + weather.getDay().getIconPhrase() +
                        ". Night - " + weather.getNight().getIconPhrase() + ".");

                WeatherData weatherData = new WeatherData(Constants.getInstance().getSelectedCity(),
                        weather.getDate().substring(0,10), weather.getDay().getIconPhrase(), weather.getNight().getIconPhrase(),
                        castFloatToDouble(weather.getTemperature().getMinimum().getValue()),  castFloatToDouble(weather.getTemperature().getMaximum().getValue())
                );

                repository.saveWeatherData(weatherData);
            }
        }

        if (periods.equals(ForecastPeriod.BASE)) {
            getAllFromDb();
        }
    }

    private Double castFloatToDouble(float value) {
        return (double) value;
    }

    public List<WeatherData> getAllFromDb() throws SQLException, IOException {
        List<WeatherData> weatherDataList = repository.getAllSavedData();
        for (WeatherData weatherData : weatherDataList) {
            System.out.println(weatherData);
        }
        return weatherDataList;
    }

    public String detectCityKey() throws IOException {
        String selectedCity = Constants.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
            .scheme("http")
            .host(BASE_HOST)
            .addPathSegment("locations")
            .addPathSegment(API_VERSION)
            .addPathSegment("cities")
            .addPathSegment("autocomplete")
            .addQueryParameter("apikey", API_KEY)
            .addQueryParameter("q", selectedCity)
            .build();

        Request request = new Request.Builder()
            .addHeader("accept", "application/json")
            .url(detectLocationURL)
            .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("It's impossible to verify city info " +
                "Status code is " + response.code() + " response body = " + response.body().string());
        }
        String jsonResponse = response.body().string();

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }

    public String trimBrackets(JsonNode string){
        int lastIndexBody = string.toString().lastIndexOf("\"");
        String newString = string.toString().substring(1, lastIndexBody);
        return newString;
    }
}
