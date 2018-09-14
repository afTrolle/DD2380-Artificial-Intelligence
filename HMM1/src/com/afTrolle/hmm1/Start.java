package com.afTrolle.hmm1;

import com.afTrolle.common.HMM2;
import com.afTrolle.common.Matrix;
import com.afTrolle.common.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Start {

    public static void main(String[] args) {


        try {
            Parser parser = new Parser("HMM1/res/input.in");

            Matrix<Double> aMatrix = parser.parseMatrix();
            Matrix<Double> bMatrix = parser.parseMatrix();
            Matrix<Double> piMatrix = parser.parseMatrix();

            HMM2 hmm = new HMM2(aMatrix, bMatrix, piMatrix);


            int[] stateTransitions = parser.parseArray();
          //  double res = hmm.getLikelihoodOfObservationSeq(stateTransitions);
            double res = hmm.backward(stateTransitions);
            System.out.println(res);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
