import com.afTrolle.common.HMM;
import com.afTrolle.common.Matrix;
import com.afTrolle.common.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Start {

    public static void main(String[] args) {
        try {
            Parser parser = new Parser("HMM3/res/input.in");

            Matrix<Double> aMatrix = parser.parseMatrix();
            Matrix<Double> bMatrix = parser.parseMatrix();
            Matrix<Double> piMatrix = parser.parseMatrix();

            HMM hmm = new HMM(aMatrix, bMatrix, piMatrix);

            //observation sequence
            int[] emissionSequence = parser.parseArray();
         // double res = hmm.forward(emissionSequence);
            double res = 0;
            System.out.println("forward done:");
            double res2 = hmm.backward(emissionSequence);
            System.out.println("backward: "+res2);
            System.out.println("forward: "+res);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
