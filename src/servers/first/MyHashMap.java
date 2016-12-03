package servers.first;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by masseeh on 10/4/16.
 */
public class MyHashMap<K,V> extends HashMap<K,V> { 

    public MyHashMap() {
		super();
	}

	public MyHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}

	public synchronized V tryPut(K key,V value) {
        if (!this.containsKey(key)) {
            return this.put(key,value);
        }
        return this.get(key);
    }
	
	public synchronized V tryRemove(K key) {
		return this.remove(key);
	}

}
