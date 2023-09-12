package strings;


import common.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// represents a single configuration: the start string and its finished solution
public class StringsConfig implements Configuration{

    private String current;
    public String finish;


    // constructor assigns first string and solution
    public StringsConfig(String start, String end){
        current = start;
        finish = end;
    }

    // if the current configuration is equal to the solution then return true
    @Override
    public boolean isSolution() {
        boolean isSolution = false;
        if (this.current.equals(finish)){
            isSolution = true;
        }
        return isSolution;

    }

    // return the two 'neighbors' of each letter
    // e.g.: the neighbors of A are B and Z
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();

        // loop for each letter in configuration
        for (int i = 0; i < current.toString().length(); i++){
            // build string for both types
            StringBuilder backwards = new StringBuilder(this.current);
            StringBuilder forwards = new StringBuilder(this.current);
            char temp = this.current.charAt(i);
            // special cases occur for the start and end of alphabet
            if (temp == 'A') {
                backwards.setCharAt(i, 'Z');
                forwards.setCharAt(i, 'B');
            }
            else if (temp == 'Z'){
                forwards.setCharAt(i, 'A');
                backwards.setCharAt(i, 'Y');
            }
            else {
                backwards.setCharAt(i, (char) (temp - 1));
                forwards.setCharAt(i, (char) (temp + 1));
            }
            // create configuration so it can be added to the path
            StringsConfig back = new StringsConfig(backwards.toString(), finish);
            StringsConfig forward = new StringsConfig(forwards.toString(), finish);
            neighbors.add(back);
            neighbors.add(forward);
        }

        // return configurations
        return neighbors;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringsConfig that = (StringsConfig) o;
        return Objects.equals(current, that.current);
    }

    // generate and return hashcode
    @Override
    public int hashCode() {
        return Objects.hash(current);
    }

    // current is a string so just return it
    @Override
    public String toString(){
        return current;
    }

    @Override
    public int[][] returnBoard() {
        return null;
    }

}


