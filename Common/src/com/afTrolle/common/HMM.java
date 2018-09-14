package com.afTrolle.common;


public class HMM {


    //transition matrix,  the matrix that gives the probability to transition from one state to another
    Matrix<Double> aMatrix;

    //emission/observation matrix, the matrix that gives the probability for the different emissions / events / observations given a certain state
    Matrix<Double> bMatrix;

    //initial-state matrix, initial state distribution, probability of starting in a specific state.
    Matrix<Double> piMatrix;

    //number of States
    final int numStates;
    //number of Observations that can be made
    final int numObservations;

    //distinct states of the Markov process
    final int[] Q;
    //set of possible observations
    final int[] V;

    public HMM(Matrix<Double> aMatrix, Matrix<Double> bMatrix, Matrix<Double> piMatrix) {
        this.aMatrix = aMatrix;
        this.bMatrix = bMatrix;
        this.piMatrix = piMatrix;

        numStates = aMatrix.getColLength();
        numObservations = bMatrix.getColLength();

        Q = new int[numStates];
        V = new int[numObservations];

        for (int i = 0; i < numStates; i++) {
            Q[i] = i;
        }

        for (int i = 0; i < numObservations; i++) {
            V[i] = i;
        }
    }

    private Double getFromAToA(int fromState, int toState){
       return aMatrix.get(fromState,toState);
    }

    private Double getInStateChanceToObserv(int state,int observation){
        return bMatrix.get(state,observation);
    }


    public void printHMM() {
        aMatrix.printMatrix("from state/to state , A-matrix");
        bMatrix.printMatrix("state/observation , B-matrix");
        piMatrix.printMatrix("probability of initial state , Pi-matrix");
    }


    // multiples aMatrix each col with init array.
    // think it gives avg time spent in each state.
    public Double[] getLikleyFirstObservation() {
        int colLen = piMatrix.getColLength();
        Double[] res = new Double[colLen];
        //Can only be one row
        Double[] piZeroRow = piMatrix.getRow(0);
        for (int i = 0; i < colLen; i++) {
            Double[] bCol = aMatrix.getColumn(i);
            res[i] = MatrixHelper.elemmentWiseProductSum(piZeroRow, bCol);
        }
        return res;
    }


    public Double[] getObservationEmission() {
        Double[] likleyFirstObservation = getLikleyFirstObservation();
        int bColLen = bMatrix.getColLength();
        Double[] ret = new Double[bColLen];
        for (int i = 0; i < bColLen; i++) {
            Double[] col = bMatrix.getColumn(i);
            Double sum = 0D;
            for (int j = 0; j < col.length; j++) {
                sum += likleyFirstObservation[j] * col[j];
            }
            ret[i] = sum;
        }

        return ret;
    }


    public double getLikelihoodOfObservationSeq(int[] stateTransitions) {
        //index of observation
        int index = 0;
        Double[] doubles = bColWithElementWiseProductWithArray(stateTransitions[index], getPiArray());
        System.out.println(MatrixHelper.indexOfMax(doubles));

        for (index = 1; index < stateTransitions.length; index++) {

            Double[] probArray = new Double[doubles.length];

            for (int i = 0; i < doubles.length; i++) {
                Double[] column = aMatrix.getColumn(i);
                Double prob = MatrixHelper.elemmentWiseProductSum(doubles, column);
                probArray[i] = prob;
            }

            doubles = bColWithElementWiseProductWithArray(stateTransitions[index], probArray);

            System.out.println(MatrixHelper.indexOfMax(doubles));
        }

        return MatrixHelper.sumElements(doubles);
    }


    public int[][] getMostLikeleyStatesChangesOfObservationSeq(int[] stateTransitions) {
        //index of observation
        int[] res = new int[stateTransitions.length];
        int index = 0;
        Double[] doubles = bColWithElementWiseProductWithArray(stateTransitions[index], getPiArray());
        res[index] = MatrixHelper.indexOfMax(doubles);

        Double[][] probMatrix = new Double[stateTransitions.length][];
        int[][] probStateMatrix = new int[stateTransitions.length - 1][];

        probMatrix[0] = doubles;

        for (index = 1; index < stateTransitions.length; index++) {

            //list of max probability
            Double[] probArray = new Double[aMatrix.getColLength()];
            int[] probStateArray = new int[aMatrix.getColLength()];

            for (int i = 0; i < aMatrix.getColLength(); i++) {

                Double[] tempProbArray = calcRow(doubles, aMatrix.getColumn(i), bMatrix.get(i, stateTransitions[index]));
                int argMaxState = MatrixHelper.indexOfMax(tempProbArray);
                if (argMaxState == -1) {
                    probArray[i] = 0D;
                } else {
                    probArray[i] = tempProbArray[argMaxState];
                }
                probStateArray[i] = argMaxState;

            }
            doubles = probArray;
            probMatrix[index] = probArray;
            probStateMatrix[index - 1] = probStateArray;

            // TODO save probArray and prob state
        }


        for (int i = 1; i < probMatrix.length; i++) {
            int indexOfMax = MatrixHelper.indexOfMaxNoneNull(probMatrix[i]);
            //   System.out.print(indexOfMax+" ");
            System.out.print(probStateMatrix[i - 1][indexOfMax] + " ");

        }
        int indexOfMax = MatrixHelper.indexOfMaxNoneNull(probMatrix[probMatrix.length - 1]);
        System.out.print(indexOfMax + " ");
        return probStateMatrix;
    }

    private Double[] calcRow(Double[] maxProbability, Double[] aCol, Double bObservation) {
        Double[] tempProb = new Double[maxProbability.length];
        for (int i = 0; i < maxProbability.length; i++) {
            tempProb[i] = maxProbability[i] * aCol[i] * bObservation;
        }
        return tempProb;
    }


    private Double[] bColWithElementWiseProductWithArray(int bColIndex, Double[] probability) {
        Double[] column = bMatrix.getColumn(bColIndex);
        return MatrixHelper.elementWiseProduct(probability, column);
    }

    //gets Pi initial  state probability as array
    private Double[] getPiArray() {
        return piMatrix.getRow(0);
    }

    public int getAColIndex() {
        double max = MatrixHelper.sumElements(aMatrix.getColumn(0));
        int index = 0;
        for (int i = 1; i < aMatrix.getColLength(); i++) {
            double column = MatrixHelper.sumElements(aMatrix.getColumn(i));
            if (max < column) {
                max = column;
                index = i;
            }
        }
        return index;
    }

    public int aMatrixFromTo(int lastIndex) {
        return MatrixHelper.indexOfMax(aMatrix.getColumn(lastIndex));
    }


    public int[] getViterbi(int[] emissionSequence) {
        //calculate most likley   z* = argmax p(z|x) , x = emissionSequence

        //rk: if f(a) >= 0 va and g(a,b) >= 0 then
        //
        // max f(a)g(a,b) = max{f(a) Max g(a,b)}


        return new int[0];
    }

    // alpha pass
    public int[] forward(int[] stateSeq) {

        return null;
    }
    // beta pass
    public int[] backward(int[] EmssionSeq) {
    return null;
    }

    // dp dynamic programming

}
