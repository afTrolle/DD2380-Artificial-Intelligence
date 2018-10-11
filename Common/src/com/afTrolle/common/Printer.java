package com.afTrolle.common;

public class Printer {


    public static void printArray(Double[] ans) {
        System.out.print("1 " + ans.length);
        for (int i = 0; i < ans.length; i++) {
            System.out.print(" " + ans[i]);
        }
        System.out.println();
    }

    public static void printMatrix(double[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        System.out.print(row + " " + col + " ");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(matrix[i][j] + " ");
            }
        }
        System.out.println();


    }
}
