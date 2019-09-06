package dk.jyskebank.tools.enunciate.modules.openapi.multi_dim_array;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.webcohesion.enunciate.metadata.rs.TypeHint;

import java.util.List;

@JsonRootName("multiDimArraySupport")
public class DataDTO {
    private String name;

    private List<int[]> temperatures;

    private int[] locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @TypeHint(int[][].class)
    public List<int[]> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<int[]> temperatures) {
        this.temperatures = temperatures;
    }

    public int[] getLocations() {
        return locations;
    }

    public void setLocations(int[] locations) {
        this.locations = locations;
    }
}
