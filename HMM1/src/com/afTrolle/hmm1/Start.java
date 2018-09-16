package com.afTrolle.hmm1;

import com.afTrolle.common.HMM;
import com.afTrolle.common.Matrix;
import com.afTrolle.common.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Start {

    public static void main(String[] args) {


        try {
            Parser parser = new Parser(null);

            Matrix<Double> aMatrix = parser.parseMatrix();
            Matrix<Double> bMatrix = parser.parseMatrix();
            Matrix<Double> piMatrix = parser.parseMatrix();

            HMM hmm = new HMM(aMatrix, bMatrix, piMatrix);


            int[] stateTransitions = parser.parseArray();
            double res = hmm.forward(stateTransitions);
            System.out.println(res);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
