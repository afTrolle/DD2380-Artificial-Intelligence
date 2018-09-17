package com.afTrolle.common;

import java.lang.reflect.Array;

public class Matrix<T> {

    private T[][] matrix;
    Class test;

    public Matrix(T[][] matrix) {
        this.matrix = matrix;
    }

    public T[][] getMatrix() {
        return matrix;
    }

    public T[] getRow(int indexOfRow) {
        return matrix[indexOfRow];
    }

    public T[] getColumn(int indexOfColumn) {
        @SuppressWarnings("unchecked")
        T[] ret = (T[]) Array.newInstance(matrix[0][0].getClass(), matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            T matrixVal = matrix[i][indexOfColumn];
            ret[i] = matrixVal;
        }
        return ret;
    }


    public void printMatrix(String name) {
        System.out.print('\n');
        System.out.println(" " + name);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" " + matrix[i][j] + " ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }


    public int getColLength() {
        return matrix[0].length;
    }

    public T get(int i, int j) {
        return matrix[i][j];
    }

    public void printOutput() {
    int rowLen = matrix.length;
    int colLen = getColLength();
    System.out.print(rowLen +" " + colLen +" ");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
        }
    System.out.println();
    }

}
