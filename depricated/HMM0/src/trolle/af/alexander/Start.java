package trolle.af.alexander;

import trolle.af.alexander.hmmm1.Test;

import java.io.*;

public class Start {

    public static void main(String[] args) {
	// write your code here

        //BufferedReader br = new BufferedReader(new FileReader("res/input.in"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String a = br.readLine();
            String b = br.readLine();
            String pi = br.readLine();

            double[][] aMatrix = Parser.parseLine(a);
            double[][] bMatrix = Parser.parseLine(b);
            double[][] piMatrix = Parser.parseLine(pi);



            HMM hmm = new HMM(aMatrix,bMatrix,piMatrix);
       //     hmm.printMatrixs();

//            hmm.getEmissionProbabilityDistribution();

          //  System.out.println(" Likley");

            double[] likleyFirstObservation = hmm.getLikleyFirstObservation();
            double[] res = hmm.getObservationEmission(likleyFirstObservation);


            System.out.print("1 "+res.length);
            for (int i = 0; i < res.length ; i++) {
                System.out.print(" "+res[i]);
            }
            Test.test();


         //   System.out.println("done");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
