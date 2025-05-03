package org.myNeuroV2;

public class Utils {
    /**
     * Clones the provided array
     *
     * @param src
     * @return a new clone of the provided array
     */
    public static double[][] cloneArray(double[][] src) {
        int length = src.length;
        double[][] target = new double[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
}
