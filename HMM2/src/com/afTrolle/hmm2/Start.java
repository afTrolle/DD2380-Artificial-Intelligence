package com.afTrolle.hmm2;

import com.afTrolle.common.*;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("HMM2/res/input.in");

        double[][] aMatrix = parser.parseMatrix();
        double[][] bMatrix = parser.parseMatrix();
        double[][] piMatrix = parser.parseMatrix();
        int[] sequenceOfObservations = parser.parseArray();
        HM hmm = new HM(aMatrix, bMatrix, piMatrix);

        hmm.hmm2(sequenceOfObservations);
    }
}
