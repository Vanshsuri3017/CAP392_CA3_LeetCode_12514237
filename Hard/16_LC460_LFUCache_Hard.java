
import java.util.*;

class LFUCache {

    class Node {
        int key, value, freq;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    int capacity;
    int minFreq;

    HashMap<Integer, Node> map;
    HashMap<Integer, LinkedHashSet<Integer>> freqMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        minFreq = 0;

        map = new HashMap<>();
        freqMap = new HashMap<>();
    }

    public int get(int key) {

        if (!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        updateFreq(node);

        return node.value;
    }

    public void put(int key, int value) {

        if (capacity == 0) {
            return;
        }

        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            updateFreq(node);
            return;
        }

        if (map.size() == capacity) {

            LinkedHashSet<Integer> keys = freqMap.get(minFreq);

            int removeKey = keys.iterator().next();

            keys.remove(removeKey);
            map.remove(removeKey);
        }

        Node node = new Node(key, value);

        map.put(key, node);

        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);

        minFreq = 1;
    }

    private void updateFreq(Node node) {

        int oldFreq = node.freq;

        LinkedHashSet<Integer> keys = freqMap.get(oldFreq);
        keys.remove(node.key);

        if (keys.isEmpty()) {
            freqMap.remove(oldFreq);

            if (minFreq == oldFreq) {
                minFreq++;
            }
        }

        node.freq++;

        freqMap.computeIfAbsent(
            node.freq, k -> new LinkedHashSet<>()
        ).add(node.key);
    }
}
