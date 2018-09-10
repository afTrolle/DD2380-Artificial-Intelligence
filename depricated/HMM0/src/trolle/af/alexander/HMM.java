package trolle.af.alexander;

public class HMM {

    public HMM(int numStates, int numEvents) {
        alpha = new double[numStates][numStates];
        beta = new double[numStates][numEvents];
        pi = new double[1][numStates];
    }

    //transition matrix,  the matrix that gives the probability to transition from one state to another
    double[][] alpha;

    //emission matrix, the matrix that gives the probability for the different emissions / events / observations given a certain state
    double[][] beta;

    //initial-state matrix, probability of starting in a specific state.
    double[][] pi;


    public HMM(double[][] aMatrix, double[][] bMatrix, double[][] piMatrix) {
        alpha = aMatrix;
        beta = bMatrix;
        pi = piMatrix;
    }



    public void printAMatrix() {
        printMatrix("from state/to state , A-matrix", alpha);
    }

    public void printBMatrix() {
        printMatrix("state/observation , B-matrix", beta);
    }

    public void printPiMatrix() {
        printMatrix("probability of initial state , Pi-matrix", pi);
    }

    public double[][] getEmissionProbabilityDistribution() {

        //test[row][col];
        //test[0];

        for (int i = 0; i < alpha.length; i++) {
            double[] row = getRow(i, alpha);

        }

        return new double[0][];
    }

    public double[] elementWiseProduct(double[] a, double[] b) {
        if (a.length != b.length) {
            return null;
        }
        double ret[] = new double[a.length];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = a[i] * b[i];
        }
        return ret;
    }

    public void printMatrixs() {
        printAMatrix();
        printBMatrix();
        printPiMatrix();
    }

    public double[] getLikleyFirstObservation() {

        double[] ret = new double[pi[0].length];
        for (int i = 0; i < pi[0].length; i++) {
            double[] bCol = getCol(i, alpha);
            double sum = 0;
            for (int j = 0; j < bCol.length; j++) {
                sum += pi[0][j] * bCol[j];
            }
            ret[i] = sum;
        }

        return ret;
    }

    public double[] getObservationEmission(double[] likleyFirstObservation) {
        int betaCols = beta[0].length;
        double[] ret = new double[betaCols];
        for (int i = 0; i < betaCols; i++) {
            double[] col = getCol(i, beta);
            double sum = 0;
            for (int j = 0; j < col.length; j++) {
               sum+= likleyFirstObservation[j] * col[j];
            }
            ret[i] = sum;
        }

        return ret;
    }

    /* handeled */


    public static void printMatrix(String name, double[][] matrix) {
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

    private double[] getRow(int row, double[][] matrix) {
        return matrix[row];
    }

    private double[] getCol(int col, double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double matrixVal = matrix[i][col];
            ret[i] = matrixVal;
        }
        return ret;
    }

}
