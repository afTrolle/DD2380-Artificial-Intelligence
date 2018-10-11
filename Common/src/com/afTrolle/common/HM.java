package com.afTrolle.common;

public class HM {

    //transition matrix,  the matrix that gives the probability to transition from one state to another
    double[][] aMatrix;
    double[][] aTransMatrix;

    //emission/observation matrix, the matrix that gives the probability for the different emissions / events / observations given a certain state
    double[][] bMatrix;
    double[][] bTransMatrix;

    //initial-state matrix, initial state distribution, probability of starting in a specific state.
    double[][] piMatrix;

    public int numStates;
    public int numUniqueObservations;

    public void setaMatrix(double[][] aMatrix) {
        numStates = aMatrix.length;
        this.aMatrix = aMatrix;
        aTransMatrix = new double[aMatrix[0].length][aMatrix.length];
        for (int i = 0; i < aTransMatrix.length; i++) {
            for (int j = 0; j < aTransMatrix[0].length; j++) {
                aTransMatrix[i][j] = aMatrix[j][i];
            }
        }
    }

    public void setbMatrix(double[][] bMatrix) {
        //number of cols
        numUniqueObservations = bMatrix[0].length;
        this.bMatrix = bMatrix;
        bTransMatrix = new double[bMatrix[0].length][bMatrix.length];
        for (int i = 0; i < bTransMatrix.length; i++) {
            for (int j = 0; j < bTransMatrix[0].length; j++) {
                bTransMatrix[i][j] = bMatrix[j][i];
            }
        }
    }

    public void setPiMatrix(double[][] piMatrix) {
        this.piMatrix = piMatrix;
    }

    public HM(double[][] a, double[][] b, double[][] pi) {
        setaMatrix(a);
        setbMatrix(b);
        setPiMatrix(pi);
    }

    /*
     *
     *  AI functions
     *
     */


    /*    Alpha functions      */

    private double[][] alphaOneNorm(int[] observation) {
        double[][] ret = new double[observation.length][numStates];
        double[] bCol = getBCol(observation[0]);
        double[] piMatrix = this.piMatrix[0];
        ret[0] = multiply(bCol, piMatrix);
        double v = sumArray(ret[0]);
        ret[0] = divide(ret[0], v);
        return ret;
    }


    //correct without normalization
    private double[][] alphaNorm(int[] observations) {
        double[][] ans = alphaOneNorm(observations);

        for (int i = 1; i < observations.length; i++) {
            double[] temp = new double[numStates];
            for (int j = 0; j < numStates; j++) {
                double[] aCol = getACol(j);
                temp[j] = multiplySum(ans[i - 1], aCol);
            }
            double[] bCol = getBCol(observations[i]);

            ans[i] = multiply(temp, bCol);

            //normalization
            double v = sumArray(ans[i]);
            ans[i] = divide(ans[i], v);
        }

        return ans;
    }


    private double[][] alphaOneMarginalized(int[] observation) {
        double[][] ret = new double[observation.length][numStates];
        double[] bCol = getBCol(observation[0]);
        double[] piMatrix = this.piMatrix[0];
        ret[0] = multiply(bCol, piMatrix);
        return ret;
    }


    //correct without normalization
    private double[][] alphaMarginalizedIterative(int[] observations) {
        double[][] ans = alphaOneMarginalized(observations);

        for (int i = 1; i < observations.length; i++) {
            double[] temp = new double[numStates];
            for (int j = 0; j < numStates; j++) {
                double[] aCol = getACol(j);
                temp[j] = multiplySum(ans[i - 1], aCol);
            }
            double[] bCol = getBCol(observations[i]);

            ans[i] = multiply(temp, bCol);

            //normalization
            // double v = sumArray(ans[i]);
            // ans[i] = divide(ans[i], v);
        }

        return ans;
    }


    /*   beta functions    */


    public double[] betaStart(int[] observations) {

        double[] ans = new double[numStates];
        for (int i = 0; i < numStates; i++) {
            ans[i] = beta(observations, -1, i);
        }
        return ans;
    }

    public double beta(int[] observations, int t, int i) {
        if (t == observations.length - 1) {
            return 1D;
        }

        double sum = 0;

        //marginalize over states
        for (int j = 0; j < numStates; j++) {
            double beta = beta(observations, t + 1, j);
            int observation = observations[t + 1];
            double b = this.bMatrix[j][observation];
            double a = this.aMatrix[i][j];
            sum += beta * b * a;
        }

        return sum;
    }

    public double[][] betaNormalized(int[] observations) {

        //init step TIME T
        double[][] res = new double[observations.length + 1][numStates];
        for (int i = 0; i < numStates; i++) {
            res[observations.length][i] = 1D;
        }

        //go in reverse, not completely sure if needed...
        //calcutlate one state

        for (int t = observations.length - 1; t >= 0; t--) {
            int observation = observations[t];
            double[] temp = new double[numStates];
            //for each i
            for (int i = 0; i < numStates; i++) {
                double sum = 0;
                for (int j = 0; j < numStates; j++) {
                    sum = sum + res[t + 1][j] * aMatrix[i][j] * bMatrix[j][observation];
                }
                temp[i] = sum;
            }
            res[t] = temp;

            //  double v = sumArray(res[t]);
            //  res[t] = divide(res[t], v);
        }

        return res;
    }

    public double[][] betaMarginalized(int[] observations) {

        //init step TIME T
        double[][] res = new double[observations.length + 1][numStates];
        for (int i = 0; i < numStates; i++) {
            res[observations.length][i] = 1D;
        }

        //go in reverse, not completely sure if needed...
        //calcutlate one state

        for (int t = observations.length - 1; t >= 0; t--) {
            int observation = observations[t];
            double[] temp = new double[numStates];
            //for each i
            for (int i = 0; i < numStates; i++) {
                double sum = 0;
                for (int j = 0; j < numStates; j++) {
                    sum = sum + res[t + 1][j] * aMatrix[i][j] * bMatrix[j][observation];
                }
                temp[i] = sum;
            }
            res[t] = temp;

            //  double v = sumArray(res[t]);
            //  res[t] = divide(res[t], v);
        }

        return res;
    }

    /*   delta functions    */

    public DeltaRet[] deltaOne(int state, int obesrvation, int size) {
        double v = bMatrix[state][obesrvation] * piMatrix[0][state];
        DeltaRet ret = new DeltaRet();
        ret.max = v;
        ret.argMax = state;
        DeltaRet[] rets = new DeltaRet[size];
        rets[0] = ret;
        return rets;
    }

    public class DeltaRet {
        int argMax = -1;
        double max = -1;
    }

    public DeltaRet[] delta(int[] observations, int t, int i) {

        if (t == 0) {
            return deltaOne(i, observations[0], observations.length + 1);
        }
        DeltaRet ret = new DeltaRet();
        DeltaRet[] save = null;


        for (int j = 0; j < numStates; j++) {

            DeltaRet[] deltaPervs = delta(observations, t - 1, j);
            DeltaRet deltaPrev = deltaPervs[t - 1];
            double a = this.aMatrix[j][i];
            double b = this.bMatrix[i][observations[t]];
            double d = deltaPrev.max * a * b;

            if (ret.max < d) {
                save = deltaPervs;
                ret.max = d;
                ret.argMax = j;
            }
        }

        save[t] = ret;
        return save;
    }

    public void deltaStart(int[] observatrions) {

        int indexMax = 0;
        double max = 0;
        DeltaRet[] deltaSave = null;
        for (int i = 0; i < numStates; i++) {
            final DeltaRet[] delta = delta(observatrions, observatrions.length - 1, i);

            if (delta[observatrions.length - 1].max > max) {
                max = delta[observatrions.length - 1].max;
                indexMax = i;
                deltaSave = delta;
            }
        }


        for (int i = 1; i < deltaSave.length - 1; i++) {
            System.out.print(deltaSave[i].argMax + " ");
        }

        System.out.print(indexMax);
    }


    public double[] deltaFirstStepMarginalized(int observation) {
        double[] bCol = getBCol(observation);
        double[] piRow = piMatrix[0];
        return multiply(bCol, piRow);
    }

    public int[] deltaIterative(int[] observations) {
        //used for backtracking.
        double[][] maxProb = new double[observations.length][numStates];
        int[][] argMaxs = new int[observations.length][numStates];

        // initial step  (pi)
        maxProb[0] = deltaFirstStepMarginalized(observations[0]);

        for (int i = 1; i < observations.length; i++) {
            int observation = observations[i];
            double[] bCol = getBCol(observation);

            for (int j = 0; j < numStates; j++) {
                double[] aCol = getACol(j);
                int tempState = 0;
                double tempScore = 0D;
                for (int k = 0; k < numStates; k++) {
                    double score = maxProb[i - 1][k] * bCol[j] * aCol[k];
                    if (tempScore < score) {
                        tempScore = score;
                        tempState = k;
                    }
                }

                maxProb[i][j] = tempScore;
                argMaxs[i][j] = tempState;
            }
        }

        //Next state with highest probability
        int stateWithHighestProb = indexOfHigestValue(maxProb[maxProb.length - 1]);

        //Backtracking, saving  results
        return backTrack(stateWithHighestProb, argMaxs);
    }

    private int[] backTrack(int index, int[][] argMaxs) {
        int[] res = new int[argMaxs.length];
        res[argMaxs.length - 1] = index;

        for (int i = argMaxs.length - 2; i >= 0; i--) {
            int argMax = argMaxs[i][index];
            res[i] = argMax;
            index = argMax;
        }
        return res;
    }

    private void printArray(String str, double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(str + " " + i + ": " + arr[i]);
        }
    }

    public void diGamma(int[] observations) {
        boolean run = true;
        while (run) {
            run = false;
            // should be correct
            double[][] alphaT = alphaMarginalizedIterative(observations);

            printArray("alpha", alphaT[observations.length - 1]);

            // should be correct? maybe should be in reverse order...
            double[][] betaT = betaMarginalized(observations);

            printArray("beta", betaT[0]);

            // ? maybe sho
            double[][][] diGamma = new double[observations.length][numStates][numStates];
            double[][] gammas = new double[observations.length][numStates];
            double alphaTSum = sumArray(alphaT[observations.length - 1]);

            for (int t = 0; t < observations.length - 1; t++) {
                int observation = observations[t];
                for (int i = 0; i < numStates; i++) {

                    double alphaTI = alphaT[t][i];
                    double sum = 0;
                    for (int j = 0; j < numStates; j++) {
                        double v = (alphaTI * aMatrix[i][j] * bMatrix[j][observation] * betaT[t][j]) / alphaTSum;
                        diGamma[t][i][j] = v;
                        sum += v;
                    }
                    gammas[t][i] = sum;
                }

                //  divide(gammas[t], sumArray(gammas[t]));
            }

            double aMatrixTemp[][] = new double[numStates][numStates];

            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    double diGammaSum = 0;
                    double gammaSum = 0;
                    for (int t = 1; t < observations.length - 2; t++) {
                        diGammaSum += diGamma[t][i][j];
                        gammaSum += gammas[t][i];
                    }
                    aMatrixTemp[i][j] = diGammaSum / gammaSum;
                }
            }


            double bMatrixTemp[][] = new double[numStates][numUniqueObservations];

            for (int j = 0; j < numStates; j++) {
                for (int k = 0; k < numUniqueObservations; k++) {
                    double obGammaSum = 0;
                    double gammaSum = 0;
                    for (int t = 1; t < observations.length - 2; t++) {
                        int observation = observations[t];
                        if (observation == k) {
                            obGammaSum += gammas[t][j];
                        }
                        gammaSum += gammas[t][j];
                    }
                    bMatrixTemp[j][k] = obGammaSum / gammaSum;
                }
            }


            if (checkAMatrix(aMatrixTemp)) {
                return;
            }


            //    piMatrix[0] =  divide(gammas[0], sumArray(gammas[0]));
            printMatrix("a matrix", aMatrixTemp);
            piMatrix[0] = gammas[0];
            setbMatrix(bMatrixTemp);
            setaMatrix(aMatrixTemp);
            //   printMatrix("a matrix", aMatrixTemp);

            counter++;
            if (counter % 1000 == 0) {
                //  printMatrix("a matrix", aMatrixTemp);
            }
        }
    }

    int counter = 0;

    private boolean checkAMatrix(double[][] aTemp) {
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                if (aTemp[i][j] != aMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     *
     *  AI assignments
     *
     */

    //calculate the probability to see next emission after first time step.
    public void hmm0() {
        double[][] ans = multiply(piMatrix, aMatrix);
        ans = multiply(ans, bMatrix);
        System.out.print(ans.length + " " + ans[0].length + " ");
        for (int i = 0; i < ans[0].length; i++) {
            System.out.print(ans[0][i] + " ");
        }
    }

    //calculate the probability to observe a certain emission sequence
    public void hmm1(int[] observations) {
        double[][] doubles = alphaMarginalizedIterative(observations);
        double v = sumArray(doubles[observations.length - 1]);
        System.out.println(v);
    }

    //calculate the most likely sequence of (hidden) states that the system moves through given an emission sequence
    public void hmm2(int[] observations) {
        deltaStart(observations);
    }

    //Given is starting guess of a HMM and a sequence of emissions.
    // you should train the HMM to maximize the probability of observing the given sequence of emissions.
    public void hmm3(int[] sequenceOfObservations) {
        diGamma(sequenceOfObservations);
    }



    /*
     *
     *  Utility functions
     *
     */


    // This is A*B = AB
    //order is important!
    public double[][] multiply(double[][] a, double[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bRows = b.length;
        int bCols = b[0].length;

        if (aCols != bRows) {
            throw new ArithmeticException("Error invalid matrix size");
        }

        double[][] res = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                Double tempRes = 0D;
                for (int k = 0; k < aCols; k++) {
                    tempRes += a[i][k] * b[k][j];
                }
                res[i][j] = tempRes;
            }
        }
        return res;
    }

    private double[] multiply(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new ArithmeticException("bad input, not same length");
        }
        double[] ans = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ans[i] = a[i] * b[i];
        }
        return ans;
    }

    //multiplies each elements with matching index and sums them.
    private double multiplySum(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new ArithmeticException("bad input, not same length");
        }
        double ans = 0;
        for (int i = 0; i < a.length; i++) {
            ans += a[i] * b[i];
        }
        return ans;
    }

    private double sumArray(double[] a) {
        double sum = 0;
        for (double v : a) {
            sum += v;
        }
        return sum;
    }

    private int indexOfHigestValue(double[] a) {
        int index = 0;
        double max = a[0];

        for (int i = 1; i < a.length; i++) {
            double temp = a[i];
            if (temp > max) {
                max = temp;
                index = i;
            }
        }

        return index;
    }

    private double[] getBCol(int index) {
        return bTransMatrix[index];
    }

    private double[] getACol(int index) {
        return aTransMatrix[index];
    }

    private double[] divide(double[] an, double v) {
        for (int i = 0; i < an.length; i++) {
            an[i] = an[i] / v;
        }
        return an;
    }

    private double[] divide(double[] an, double[] v) {
        for (int i = 0; i < an.length; i++) {
            an[i] = an[i] / v[i];
        }
        return an;
    }


    public void printMatrix(String name, double[][] matrix) {
        System.out.print('\n');
        System.out.println(" " + name);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" " + matrix[i][j] + " ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }


    private double[] multiply(double[] doubles, double v) {
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = doubles[i] * v;
        }
        return doubles;
    }


    private final int maxEstimations = 1000000;
    double prevLogProb = Double.MAX_VALUE;

    public void estitamteModel(int[] observations) {

        counter = 0;
        boolean run = true;
        while (run) {


            double[] constantsT = new double[observations.length];

            //alpha first step T = 0
            double[][] alphaT = new double[observations.length][numStates];
            alphaT[0] = multiply(getBCol(observations[0]), this.piMatrix[0]);
            constantsT[0] = 1 / sumArray(alphaT[0]);
            alphaT[0] = multiply(alphaT[0], constantsT[0]);

            //alpha  t > 0
            for (int t = 1; t < observations.length; t++) {
                constantsT[t] = 0;
                for (int i = 0; i < numStates; i++) {
                    alphaT[t][i] = 0;
                    for (int j = 0; j < numStates; j++) {
                        alphaT[t][i] += alphaT[t - 1][j] * aMatrix[j][i];

                    }
                    alphaT[t][i] = alphaT[t][i] * bMatrix[i][observations[t]];
                    constantsT[t] += constantsT[t] + alphaT[t][i];
                }

                //calc scale
                constantsT[t] = 1 / constantsT[t];
                //scale Alpha
                alphaT[t] = multiply(alphaT[t], constantsT[t]);
            }


            // beta
            double[][] betaT = new double[observations.length][numStates];
            // beta t = T-1, aka first step.
            for (int i = 0; i < numStates; i++) {
                betaT[observations.length - 1][i] = constantsT[observations.length - 1];
            }

            //Beta when t < T-1
            //might be to zero indicating that we skip zero
            for (int t = observations.length - 2; t >= 0; t--) {

                for (int i = 0; i < numStates; i++) {
                    betaT[t][i] = 0;
                    for (int j = 0; j < numStates; j++) {
                        betaT[t][i] = betaT[t][i] + aMatrix[i][j] * bMatrix[j][observations[t + 1]] * betaT[t + 1][j];
                    }

                    //scale, using scaling from alpha. same nomalization()
                    betaT[t][i] = betaT[t][i] * constantsT[t];
                }
            }


            double[][][] diGamma = new double[observations.length][numStates][numStates];
            double[][] gamma = new double[observations.length][numStates];
            //Di gamma and gama calc

            for (int t = 0; t < observations.length - 1; t++) {

                //calc denorm
                double denorm = 0;
                for (int i = 0; i < numStates; i++) {
                    for (int j = 0; j < numStates; j++) {
                        denorm += alphaT[t][i] * aMatrix[i][j] * bMatrix[j][observations[t + 1]] * betaT[t + 1][j];
                    }
                }

                for (int i = 0; i < numStates; i++) {
                    gamma[t][i] = 0;
                    for (int j = 0; j < numStates; j++) {
                        diGamma[t][i][j] = (alphaT[t][i] * aMatrix[i][j] * bMatrix[j][observations[t + 1]] * betaT[t + 1][j]) / denorm;
                        gamma[t][i] += diGamma[t][i][j];
                    }
                }
            }

            //de norm edge case  γT −1(i)
            double denorm = sumArray(alphaT[observations.length - 1]);
            for (int i = 0; i < numStates; i++) {
                gamma[observations.length - 1][i] = alphaT[observations.length - 1][i] / denorm;
            }


            // re estitamte
            //re estimate pi
            piMatrix[0] = gamma[0];


            //re estimate A
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    double num = 0;
                    double denom = 0;
                    for (int t = 0; t < observations.length - 1; t++) {
                        num += diGamma[t][i][j];
                        denom += gamma[t][i];
                    }
                    aMatrix[i][j] = num / denom;
                }
            }

            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numUniqueObservations; j++) {
                    double num = 0;
                    double denom = 0;
                    for (int t = 0; t < observations.length - 1; t++) {
                        if (observations[t] == j) {
                            num += gamma[t][i];
                        }
                        denom += gamma[t][i];
                    }
                    bMatrix[i][j] = num / denom;
                }
            }

            // compute  log[P(O | λ)]
            double logProb = 0;
            for (int t = 0; t < observations.length; t++) {
                logProb += Math.log(constantsT[t]);
            }

            // update completed
            counter++;


            if (counter < maxEstimations && logProb < prevLogProb) {
                prevLogProb = logProb;
            } else {
                run = false;
            }
        }


        Printer.printMatrix(aMatrix);
        Printer.printMatrix(bMatrix);
    }

}
