import com.afTrolle.common.HM;
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
            Parser parser = new Parser(null);

            double[][] aMatrix = parser.parseMatrix();
            double[][] bMatrix = parser.parseMatrix();
            double[][] piMatrix = parser.parseMatrix();
            int[] sequenceOfObservations = parser.parseArray();
            HM hmm = new HM(aMatrix, bMatrix, piMatrix);

            hmm.estitamteModel(sequenceOfObservations);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
