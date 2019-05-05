package hw3.hash;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        boolean counter = true;
        List<Integer> oList = new ArrayList<>(Collections.nCopies(M, 0));
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            int newCount = oList.get(bucketNum) + 1;
            oList.set(bucketNum, newCount);
        }

        int total = oomages.size();

        for (Integer val: oList) {
            double valD = val * 1.0;
            if (val < total / 50 || valD > total / 2.5) {
                counter = false;
            }
        }
        return counter;
    }
}
