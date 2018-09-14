package com.afTrolle.hmm2;

import com.afTrolle.common.*;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser("HMM2/res/input.in");

        Matrix<Double> aMatrix = parser.parseMatrix();
        Matrix<Double> bMatrix = parser.parseMatrix();
        Matrix<Double> piMatrix = parser.parseMatrix();

        HMM2 hmm = new HMM2(aMatrix, bMatrix, piMatrix);

        //Observations seen in sequence
        int[]  emissionSequence = parser.parseArray();

        hmm.backward(emissionSequence);
    }
}
