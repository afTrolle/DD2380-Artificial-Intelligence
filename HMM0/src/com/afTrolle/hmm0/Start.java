package com.afTrolle.hmm0;

import com.afTrolle.common.*;

import java.io.IOException;

public class Start {

    public static void main(String[] args) {

        try {
            Parser parser = new Parser("HMM0/res/input.in");
            double[][] aMatrix = parser.parseMatrix();
            double[][] bMatrix = parser.parseMatrix();
            double[][] piMatrix = parser.parseMatrix();
            HM hmm = new HM(aMatrix, bMatrix, piMatrix);
            hmm.hmm0();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
