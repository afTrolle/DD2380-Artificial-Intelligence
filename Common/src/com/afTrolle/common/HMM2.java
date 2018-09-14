package com.afTrolle.common;

public class HMM2 {


    //transition matrix,  the matrix that gives the probability to transition from one state to another
    Matrix<Double> aMatrix;

    //emission/observation matrix, the matrix that gives the probability for the different emissions / events / observations given a certain state
    Matrix<Double> bMatrix;

    //initial-state matrix, initial state distribution, probability of starting in a specific state.
    Matrix<Double> piMatrix;

    //N ,number of States
    final int numStates;
    //M ,number of Observation symbols that can be seen.
    final int numObservations;

    //distinct states of the Markov process
    final int[] Q;
    //set of possible observations
    final int[] V;

    public HMM2(Matrix<Double> aMatrix, Matrix<Double> bMatrix, Matrix<Double> piMatrix) {
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

    public void printHMM() {
        aMatrix.printMatrix("from state/to state , A-matrix");
        bMatrix.printMatrix("state/observation , B-matrix");
        piMatrix.printMatrix("probability of initial state , Pi-matrix");
    }


    public void start(int[] observations) {
        int observationIndex = 0;
        int observation = observations[observationIndex];


        Double[] column = bMatrix.getColumn(observation);
        Double[] piInit = piMatrix.getRow(0);
        Double[] prev = MatrixHelper.elementWiseProduct(column, piInit);

        Double[][] pathMatrix = new Double[numStates][observations.length];
        double score = 0;

        for (int i = 0; i < numStates; i++) {
            call(score, i, observationIndex + 1, observations);
        }

        System.out.println(pathExplorered);
    }


    int pathExplorered = 0;

    private double call(Double score, int currentState, int observationIndex, int[] observations) {
        if (observationIndex == observations.length - 1) {
            //maybe return cost to go there.
            pathExplorered++;
            return 0;
        }

        for (int i = 0; i < numStates; i++) {
            double newScore = call(score, i, observationIndex + 1, observations);
        }

        return 1;
    }


    //
    public double backward(int[] observations) {
        Double[] row = piMatrix.getRow(0);
        int observationIndex = 0;
        savedPath = new int[observations.length];
        int[] path = new int[observations.length];
        double sum = 0;
        for (int i = 0; i < numStates; i++) {
            path[observationIndex] = i;
            Double[] bColumn = bMatrix.getColumn(observations[observationIndex]);
            path[observationIndex] = i;
            rec(row[i] * bColumn[i], observations, observationIndex + 1, path);
        }


        for (int i : savedPath) {
            System.out.print(i + " ");
        }

        return sum;
    }

    int[] savedPath;
    double highScore = 0;

    private void rec(Double aDouble, int[] observations, int observationIndex, int[] path) {
        if (observationIndex == observations.length) {
            //stop recursion
            if (aDouble > highScore) {
                highScore = aDouble;
                for (int i = 0; i < path.length; i++) {
                    savedPath[i] = path[i];
                }
            }
            return;
        }

        //this could be moved to for loop and use index only.
         final int observation = observations[observationIndex];
         final int prevState = path[observationIndex - 1];
        for (int i = 0; i < numStates; i++) {
            double score = aDouble * bMatrix.get(i,observation) * aMatrix.get(prevState, i);
            //ignore next paths if score is zero
            if (score == 0) {
                continue;
            }
            path[observationIndex] = i;
            rec(score, observations, observationIndex + 1, path);
        }
    }

}
