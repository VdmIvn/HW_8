import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class Imperial {
    @Override
    public String toString() {
        return "Imperial{" +
                "Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                ", Value=" + Value +
                '}';
    }

    @JsonProperty(value = "Unit")
    private String Unit;

    @JsonIgnore
    private String unit;

    @JsonProperty(value = "UnitType")
    private float UnitType;

    @JsonIgnore
    private float unitType;

    @JsonProperty(value = "Value")
    private float Value;

    @JsonIgnore
    private float value;
}
