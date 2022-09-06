import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Day {
    @Override
    public String toString() {
        return "Day{" +
                "Icon=" + Icon +
                ", IconPhrase='" + IconPhrase + '\'' +
                ", HasPrecipitation=" + HasPrecipitation +
                ", PrecipitationType='" + PrecipitationType + '\'' +
                ", PrecipitationIntensity='" + PrecipitationIntensity + '\'' +
                '}';
    }

    @JsonProperty(value = "Icon")
    private float Icon;

    @JsonIgnore
    private float icon;

    @JsonProperty(value = "IconPhrase")
    private String IconPhrase;

    @JsonIgnore
    private String iconPhrase;

    @JsonProperty(value = "HasPrecipitation")
    private boolean HasPrecipitation;

    @JsonIgnore
    private boolean hasPrecipitation;

    @JsonProperty(value = "PrecipitationType")
    private String PrecipitationType;

    @JsonIgnore
    private String precipitationType;

    @JsonProperty(value = "PrecipitationIntensity")
    private String PrecipitationIntensity;

    @JsonIgnore
    private String precipitationIntensity;
}