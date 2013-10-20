import java.util.Map;
import java.util.LinkedHashMap;

public class t1 {

    final int MAX_ENTRIES = 2;

    Map cache = new LinkedHashMap(MAX_ENTRIES+1, .75F, true) {
        // This method is called just after a new entry has been added
        public boolean removeEldestEntry(Map.Entry eldest) {
	    System.out.println("eldest: " + eldest.getKey() + "=" + eldest.getValue());
            return size() > MAX_ENTRIES;
        }
    };

    public void cacheAdd(String pkey, String pstr) {
        // Add to cache
        cache.put(pkey, pstr);
    }

    public static void main(String[] args) {
        t1 ot1 = new t1();
        ot1.cacheAdd("k1", "str1");
        ot1.cacheAdd("k2", "str2");
        ot1.cacheAdd("k3", "str3");
        ot1.cacheAdd("k4", "str4");
    }

    /* –
// Get object
Object o = cache.get(key);
if (o == null && !cache.containsKey(key)) {
    // Object not in cache. If null is not a possible value in the cache,
    // the call to cache.contains(key) is not needed
}

// If the cache is to be used by multiple threads,
// the cache must be wrapped with code to synchronize the methods
cache = (Map)Collections.synchronizedMap(cache);
– */

}