package com.afTrolle.hmm1;

import com.afTrolle.common.HM;
import com.afTrolle.common.Parser;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("HMM1/res/input2.in");
        double[][] aMatrix = parser.parseMatrix();
        double[][] bMatrix = parser.parseMatrix();
        double[][] piMatrix = parser.parseMatrix();
        int[] sequenceOfObservations = parser.parseArray();
        HM hmm = new HM(aMatrix, bMatrix, piMatrix);
        hmm.hmm1(sequenceOfObservations);
        hmm.hmm3(sequenceOfObservations);
    }
}
