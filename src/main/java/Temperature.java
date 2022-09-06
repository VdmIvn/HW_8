import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Temperature {
    @Override
    public String toString() {
        return "Temperature{" +
                "Metric=" + metric +
                ", Minimum=" + minimum +
                ", Maximum=" + maximum +
                ", Imperial=" + imperial +
                '}';
    }


    @JsonProperty(value = "Metric")
    private Metric metric;

    @JsonProperty(value = "Minimum")
    private Minimum minimum;

    @JsonProperty(value = "Maximum")
    private Maximum maximum;

    @JsonProperty(value = "Imperial")
    private Imperial imperial;
}
