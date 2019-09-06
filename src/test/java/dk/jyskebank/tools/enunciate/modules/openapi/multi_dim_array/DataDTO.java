package dk.jyskebank.tools.enunciate.modules.openapi.multi_dim_array;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("multiDimArraySupport")
public class DataDTO {
    private String name;

    private int[][][] temperatures;

    private int[][] sudoku;

    private int[] locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[][][] getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(int[][][] temperatures) {
        this.temperatures = temperatures;
    }

    public int[] getLocations() {
        return locations;
    }

    public void setLocations(int[] locations) {
        this.locations = locations;
    }

    public int[][] getSudoku() {
        return sudoku;
    }
}
