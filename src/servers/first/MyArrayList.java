package servers.first;

import java.util.ArrayList;

/**
 * Created by masseeh on 10/4/16.
 */
public class MyArrayList<E> extends ArrayList<E> {

    public synchronized boolean tryAdd(E element) {
        return add(element);
    }
    
    public synchronized boolean tryRemove(E element) {
		return this.remove(element);
	}

}
