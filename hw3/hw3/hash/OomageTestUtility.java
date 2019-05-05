package hw3.hash;

import java.util.List;
import java.util.TreeMap;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        boolean counter = true;
        TreeMap<Integer, Integer> tmap = new TreeMap<>();
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            if (tmap.containsKey(bucketNum)) {
                int val = tmap.get(bucketNum);
                tmap.replace(bucketNum, val + 1);
            } else {
                tmap.put(bucketNum, 1);
            }
        }
        int total = 0;
        for (Integer val: tmap.values()) {
            total += val;
        }
        for (Integer val: tmap.values()) {
            double valD = val * 1.0;
            if (val < total / 50 || valD > total / 2.5) {
                counter = false;
            }
        }
        return counter;
    }
}
