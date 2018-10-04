package com.afTrolle.hmm1;

import com.afTrolle.common.HMM;
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

            HMM hmm = new HMM(aMatrix, bMatrix, piMatrix);


            int[] stateTransitions = parser.parseArray();
            long l = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                double res = hmm.forward(stateTransitions);
            }
            System.out.println("old time: " + (System.currentTimeMillis()-l ));
            l = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                double res = hmm.hmm1(stateTransitions);
            }
            System.out.println("hmm1 new time: " + ( System.currentTimeMillis()-l));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
