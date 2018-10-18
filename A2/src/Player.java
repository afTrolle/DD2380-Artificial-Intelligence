import java.util.*;


public class Player {


    /**
     * Performs a move
     *
     * @param gameState the current state of the board
     * @param deadline  time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }
        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        //to be honest we only care about next state, also with branching function we don't know which branch the other game states depend on.
        GameState[] gameStates = new GameState[maxDepth];
        alphaBetaPrune(gameState, 1, Double.MIN_VALUE, Double.MAX_VALUE, true, gameStates);
        return gameStates[1];
    }

    final static int maxDepth = 10;

    double alphaBetaPrune(final GameState gameState, final int depth, double alpha, double beta, final boolean isPlayer, GameState[] gameStates) {
        Vector<GameState> childStates = new Vector<GameState>();
        gameState.findPossibleMoves(childStates);
        GameState returnGameState = null;
        double v = 0;
        //   int eval = evalState(gameState, isPlayer);
        if (depth >= maxDepth || gameState.isEOG()) {
            //terminal state
            v = utilityFunc(gameState, isPlayer, depth);
            returnGameState = gameState;
        } else if (isPlayer) {
            v = Double.MIN_VALUE;
            for (GameState childState : childStates) {
                double i = alphaBetaPrune(childState, depth + 1, alpha, beta, false, gameStates);
                //    v = max(v, i);
                if (v < i) {
                    v = i;
                    returnGameState = childState;
                }
                alpha = max(alpha, v);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            v = Double.MAX_VALUE;
            for (GameState childState : childStates) {
                double i = alphaBetaPrune(childState, depth + 1, alpha, beta, true, gameStates);
                if (v > i) {
                    v = i;
                    returnGameState = childState;
                }
                beta = min(beta, v);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        gameStates[depth] = returnGameState;
        return v;
    }


    private double max(double a, double b) {
        return a > b ? a : b;
    }

    private double min(double a, double b) {
        return a > b ? b : a;
    }

    public double utilityFunc(final GameState gameState, final boolean isPlayer, int depth) {
        //think we could change it to handle both for player X and Player O with mind But according to documentation the player we should maximise for is player X
        // would be weird if we where O then we would maximize for loss, hahaha.

        //depth Score
        //depth starts at 1 and increases for each level.
        final double depthScore = 1D / depth;
        //I've weighted the score the closer, to finish the game with a win the more important the move is.
        //this also means that the closer a loss occurs the higher negative value happens.
        //this works really well with the alpha beta pruning.

        if (gameState.isEOG()) {
            if (!gameState.isXWin() && !gameState.isOWin()) {
                //draw (going for zero sum)
                return 0;
            }

            //if player x wins
            if (gameState.isXWin()) {
                return 1D * depthScore;
            } else {
                return -1D * depthScore;
            }
        }

        // could be smarter too have a real estimate function when we can't find end of game.
        //  aka 3d tic tac toe probably has to large branching function.
        return 0;
    }

}
