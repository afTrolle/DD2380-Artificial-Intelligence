import com.afTrolle.common.HMM;
import com.afTrolle.common.Matrix;
import com.afTrolle.common.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Start {

    public static void main(String[] args) {


        /*
   In this task show estimate the model parameters for an HMM.
   Given initial transition matrix, emission matrix and initial state probability distribution and a sequence of emissions

   Assignment Train the HMM to maximize the probability of observing the given sequence of emissions


Output
You should output the estimated transition matrix and emission matrix on one line
each in the same matrix format as they were given, including the dimensions.
Do not output the estimated initial state probability distribution.
         */

        try {
            Parser parser = new Parser("HMM3/res/input.in");

            Matrix<Double> aMatrix = parser.parseMatrix();
            Matrix<Double> bMatrix = parser.parseMatrix();
            Matrix<Double> piMatrix = parser.parseMatrix();

            HMM hmm = new HMM(aMatrix, bMatrix, piMatrix);

            //observation sequence
            int[] emissionSequence = parser.parseArray();
           double res = hmm.forwardOpt(emissionSequence);
     //    double res = hmm.forward(emissionSequence);
           // double res = 0;
     //       System.out.println("forward done: "+ res);
      //      hmm.backwardOpt(emissionSequence);
            //double res2 = hmm.backward(emissionSequence);
       //     System.out.println("backward: "+res2);
      //      System.out.println("forward: "+res);


            aMatrix.printOutput();
            bMatrix.printOutput();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
