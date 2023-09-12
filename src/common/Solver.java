package common;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import strings.StringsConfig;
import java.util.*;

// solver method performs breadth first search
public class Solver{


    // store total configurations whether they are unique or not
    private int totalConfigs = 1;

    // Key is the current configuration, value is the preceding confidugration
    private Map<Configuration, Configuration> predecessor = new HashMap<>();;


    // solve method takes configuration and performs BFS until solution is found or the solution DNE
    public LinkedList<Configuration> solve(Configuration start) {

        // Every node visited will have a predecessor.
        // A node is assigned a predecessor iff it has been visited,
        // the keys of our predecessor map create a visited set
        // unique configs is number of keys in map
        // null predecessor indicates this is the starting point.


        // run the BFS algorithm

        // declare queue
        LinkedList<Configuration> queue = new LinkedList<>();

        // add the passed in configuration to the queue
        queue.add(start);

        // assign null as the first preceding configuration as start is the first
        predecessor.put(start, null);

        // current configuration starts as null and the solution is false, not found
        Configuration current = null;
        boolean isFound = false;

        // loop while the queue is not empty
        while (!queue.isEmpty()) {
            // the null current is assigned to the top of the queue
            current = queue.remove(0);

            // break if the solution is found
            if (current.isSolution()){
                isFound = true;
                break;
            }

            // if the configuration is unique add it to the predecessor map
            for (Configuration nbr : current.getNeighbors()) {
                // totalconfigs always increases because uniqueness doesn't matter
                totalConfigs++;
                if (!predecessor.containsKey(nbr)) {
                    predecessor.put(nbr, current);
                    // add to the queue
                    queue.add(nbr);
                }
            }
        }

        // construct the path, if one exists.
        if (isFound == false ) {
            return null;   // we never found the finish node.
        }
        else {
            LinkedList< Configuration > path = new LinkedList<>();

            while ( current != null ) {
                path.add( 0, current ); // Reverse the direction to start->finish
                current = predecessor.get( current );
            }
            return path;
        }
    }

    // return total configs

    public int getTotalConfigs(){
        return this.totalConfigs;
    }

    // return unique configs
    public int getUniqueConfigs(){
        return predecessor.size();
    }

}


