package com.afTrolle.common;

public class MatrixHelper {

    public static Double[] elementWiseProduct(Double[] a, Double[] b) {
        if (a.length != b.length) {
            return null;
        }
        Double ret[] = new Double[a.length];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = a[i] * b[i];
        }
        return ret;
    }

    public static Double sumElements(Double[] doubles) {
        Double sum = 0D;
        for (double aDouble : doubles) {
            sum += aDouble;
        }
        return sum;
    }

    public static Double elemmentWiseProductSum(Double[] a, Double[] b) {
        Double[] doubles = MatrixHelper.elementWiseProduct(a, b);
        return MatrixHelper.sumElements(doubles);
    }

    public static int indexOfMax(Double[] probArray) {
        Double max = 0D;
        int index = -1;

        for (int i = 0; i < probArray.length; i++) {
            if (max < probArray[i]) {
                index = i;
                max = probArray[i];
            }
        }
        return index;
    }

    public static int indexOfMaxNoneNull(Double[] probArray) {
        Double max = probArray[0];
        int index = 0;

        for (int i = 1; i < probArray.length; i++) {
            if (max < probArray[i]) {
                index = i;
                max = probArray[i];
            }
        }
        return index;
    }

    public static double getMax(Double[] arr){
        int i = indexOfMax(arr);
        return arr[i];
    }



}
