package com.afTrolle.hmm2;

import com.afTrolle.common.*;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser("HMM/res/input.in");

        Matrix<Double> aMatrix = parser.parseMatrix();
        Matrix<Double> bMatrix = parser.parseMatrix();
        Matrix<Double> piMatrix = parser.parseMatrix();

        HMM hmm = new HMM(aMatrix, bMatrix, piMatrix);

        //Observations seen in sequence
        int[]  emissionSequence = parser.parseArray();

        double res = hmm.backward(emissionSequence);
        System.out.println(res);
    }
}