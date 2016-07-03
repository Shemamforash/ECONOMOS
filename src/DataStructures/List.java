package DataStructures;

import java.util.ArrayList;

/**
 * Created by Sam on 03/07/2016.
 */
public class List<T> extends ArrayList<T> {
    /**
     * Adds an element to the list if it is not already inserted
     */
    public boolean addUnique(T element){
        if(contains(element)){
            return true;
        }
        add(element);
        return false;
    }
}
