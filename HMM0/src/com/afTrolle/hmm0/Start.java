package com.afTrolle.hmm0;

import com.afTrolle.common.HMM;
import com.afTrolle.common.Matrix;
import com.afTrolle.common.Parser;
import com.afTrolle.common.Printer;

import java.io.IOException;

public class Start {

    public static void main(String[] args) {

        try {
            Parser parser = new Parser(null);
            Matrix<Double> aMatrix = parser.parseMatrix();
            Matrix<Double> bMatrix = parser.parseMatrix();
            Matrix<Double> piMatrix = parser.parseMatrix();
            HMM hmm = new HMM(aMatrix,bMatrix,piMatrix);

            Double[] observationEmission = hmm.getObservationEmission();

            Printer.printArray(observationEmission);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
