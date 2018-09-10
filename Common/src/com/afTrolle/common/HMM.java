package com.afTrolle.common;


public class HMM {

    //transition matrix,  the matrix that gives the probability to transition from one state to another
    Matrix<Double> aMatrix;

    //emission matrix, the matrix that gives the probability for the different emissions / events / observations given a certain state
    Matrix<Double> bMatrix;

    //initial-state matrix, probability of starting in a specific state.
    Matrix<Double> piMatrix;

    public HMM(Matrix<Double> aMatrix, Matrix<Double> bMatrix, Matrix<Double> piMatrix) {
        this.aMatrix = aMatrix;
        this.bMatrix = bMatrix;
        this.piMatrix = piMatrix;
    }


    public void printHMM() {
        aMatrix.printMatrix("from state/to state , A-matrix");
        bMatrix.printMatrix("state/observation , B-matrix");
        piMatrix.printMatrix("probability of initial state , Pi-matrix");
    }



    // multiples aMatrix each col with init array.
    // think it gives avg time spent in each state.
    public Double[] getLikleyFirstObservation() {
        int colLen = piMatrix.getColLength();
        Double[] res = new Double[colLen];
        //Can only be one row
        Double[] piZeroRow = piMatrix.getRow(0);
        for (int i = 0; i < colLen; i++) {
            Double[] bCol = aMatrix.getColumn(i);
            res[i] = MatrixHelper.elemmentWiseProductSum(piZeroRow,bCol);
        }
        return res;
    }


    public Double[] getObservationEmission() {
        Double[] likleyFirstObservation = getLikleyFirstObservation();
        int bColLen = bMatrix.getColLength();
        Double[] ret = new Double[bColLen];
        for (int i = 0; i < bColLen; i++) {
            Double[] col = bMatrix.getColumn(i);
            Double sum = 0D;
            for (int j = 0; j < col.length; j++) {
                sum+= likleyFirstObservation[j] * col[j];
            }
            ret[i] = sum;
        }

        return ret;
    }


}
