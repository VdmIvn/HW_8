package enumsAndConstants;

public final class Constants {

    private static Constants INSTANCE;
    private String selectedCity = null;
    private final String API_KEY = "0d1tNZJPfzzT3qGokM18FGGxAUpt7hpj";
    //private final String API_KEY = "H7XjhVub01TyiCBHV2GE10qVozYWz5NB";
    private final String DB_NAME = "application.db";

    private Constants() {
    }

    public static Constants getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Constants();
        }
        return INSTANCE;
    }

    public String getDbName() {
        return DB_NAME;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getApiKey() {
        return this.API_KEY;
    }
}


