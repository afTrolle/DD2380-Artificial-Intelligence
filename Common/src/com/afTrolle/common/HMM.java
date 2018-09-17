package com.afTrolle.common;

public class HMM {


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

    public void printHMM() {
        aMatrix.printMatrix("from state/to state , A-matrix");
        bMatrix.printMatrix("state/observation , B-matrix");
        piMatrix.printMatrix("probability of initial state , Pi-matrix");
    }


    private int[] savedPath;
    private double highScore = 0;


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
            backwardRec(row[i] * bColumn[i], observations, observationIndex + 1, path);
        }


        for (int i : savedPath) {
            System.out.print(i + " ");
        }

        return sum;
    }


    private void backwardRec(Double aDouble, int[] observations, int observationIndex, int[] path) {
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
            double score = aDouble * bMatrix.get(i, observation) * aMatrix.get(prevState, i);
            //ignore next paths if score is zero
            if (score == 0) {
                continue;
            }
            path[observationIndex] = i;
            backwardRec(score, observations, observationIndex + 1, path);
        }
    }


    public double forward(int[] observations) {
        Double[] row = piMatrix.getRow(0);
        int observationIndex = 0;
        int[] path = new int[observations.length];
        double sum = 0;
        for (int i = 0; i < numStates; i++) {
            path[observationIndex] = i;
            Double[] bColumn = bMatrix.getColumn(observations[observationIndex]);
            path[observationIndex] = i;
            sum = sum + forwardRec(row[i] * bColumn[i], observations, observationIndex + 1, path);
        }

        return sum;
    }


    private double forwardRec(Double aDouble, int[] observations, int observationIndex, int[] path) {
        if (observationIndex == observations.length) {
            //stop recursion
            return aDouble;
        }

        //this could be moved to for loop and use index only.
        final int observation = observations[observationIndex];
        final int prevState = path[observationIndex - 1];
        double sum = 0;
        for (int i = 0; i < numStates; i++) {
            double score = aDouble * bMatrix.get(i, observation) * aMatrix.get(prevState, i);
            //ignore next paths if score is zero
            if (score == 0) {
                continue;
            }
            path[observationIndex] = i;
            sum = sum + forwardRec(score, observations, observationIndex + 1, path);
        }

        return sum;
    }

    public Double[] getFirstObservationProbability() {

        // int, reverse?
        Double[] initProbabilityRow = piMatrix.getRow(0);
        Double rest[] = new Double[numStates];
        for (int i = 0; i < numStates; i++) {
            rest[i] = MatrixHelper.elemmentWiseProductSum(initProbabilityRow,aMatrix.getColumn(i));
        }

        // observations
        Double[] res = new Double[numObservations];
        for (int i = 0; i < numObservations; i++) {
            Double[] column = bMatrix.getColumn(i);
            res[i] = MatrixHelper.elemmentWiseProductSum(rest, column);
        }

        return res;
    }



    public int[] backwardOpt(int[] observations) {
        Double[] row = piMatrix.getRow(0);
        int observationIndex = 0;

        savedPath = new int[observations.length];

        int[][] path = new int[numStates][observations.length];
        double[] scores = new double[numStates];

        //temporary score each road
        double[][] buffer = new double[numStates][numStates];

        //parse first observation
        for (int i = 0; i < numStates; i++) {
            path[i][0] = i;
            Double[] bColumn = bMatrix.getColumn(observations[0]);
            scores[i] = row[i] * bColumn[i];
        }

        //recursion steps
        for (int i = 1; i < observations.length; i++) {
            //depth of observations

            final int observation = observations[i];
            final Double[] bColumn = bMatrix.getColumn(observation);

            for (int j = 0; j < numStates; j++) {

                for (int k = 0; k < numStates; k++) {
                    //explore options
                    double score = scores[j] * bMatrix.get(k, observation) * aMatrix.get(path[j][i-1], k);
                    buffer[j][k] = score;
                }


                //this is bad ignore buffer,
                //do instead count best path to state
                for (int k = 0; k < numStates; k++) {

                    int winner = 0;
                    double bestScore = 0;

                    for (int l = 0; l < numStates; l++) {
                 //       buffer[]
                    }
                }
                //pick winners
               // path[j][i] =
            }
            path[i][observationIndex] = i;
           // Double[] bColumn = bMatrix.getColumn(observations[observationIndex]);
        }

        //return optimal path
        return path[0];
    }


    public double forwardOpt(int[] emissionSequence) {
        Double[] piRow = piMatrix.getRow(0);
        //res
        Double[] res = MatrixHelper.elementWiseProduct(piRow,bMatrix.getColumn(emissionSequence[0]));
        Double[] temp = new Double[numStates];
        for (int i = 1; i < emissionSequence.length; i++) {
            Double[] obsRow = bMatrix.getColumn(emissionSequence[i]);
            for (int j = 0; j < numStates; j++) {
                Double[] col = aMatrix.getColumn(j);
                temp[j] = MatrixHelper.elemmentWiseProductSum(col, res);
            }
            res = MatrixHelper.elementWiseProduct(obsRow,temp);
        }
        return MatrixHelper.sumElements(res);
    }
}
