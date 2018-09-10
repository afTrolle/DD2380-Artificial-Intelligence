package com.afTrolle.common;

public class MatrixHelper {

    public static double[] elementWiseProduct(Double[] a, Double[] b) {
        if (a.length != b.length) {
            return null;
        }
        double ret[] = new double[a.length];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = a[i] * b[i];
        }
        return ret;
    }

    public static double sumElements(double[] doubles) {
        double sum = 0;
        for (double aDouble : doubles) {
            sum += aDouble;
        }
        return  sum;
    }

    public  static  double elemmentWiseProductSum(Double[] a, Double[] b){
        double[] doubles = MatrixHelper.elementWiseProduct(a, b);
        return MatrixHelper.sumElements(doubles);
    }
}
