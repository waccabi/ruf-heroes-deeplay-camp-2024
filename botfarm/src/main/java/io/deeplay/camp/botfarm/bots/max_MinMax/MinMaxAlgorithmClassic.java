package io.deeplay.camp.botfarm.bots.max_MinMax;

import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.mechanics.GameStage;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;
import lombok.SneakyThrows;

import java.util.List;

public class MinMaxAlgorithmClassic {
    private int maxDepth;
    private final UtilityFunction utilityFunction;

    public MinMaxAlgorithmClassic(int maxDepth, UtilityFunction utilityFunction) {
        this.maxDepth = maxDepth;
        this.utilityFunction = utilityFunction;
    }

    @SneakyThrows
    public double minMax(
            GameState root,
            int currentDepth,
            boolean isMaximizingPlayer,
            PlayerType playerType) {
        if (currentDepth == maxDepth || root.getGameStage() == GameStage.ENDED) {
            return utilityFunction.getUtility(root, playerType);
        }

        List<MakeMoveEvent> possibleMoves = root.getPossibleMoves();

        if (isMaximizingPlayer) {
            if (possibleMoves.isEmpty()) {
                GameState newGameState = root.getCopy();
                newGameState.changeCurrentPlayer();
                return minMax(newGameState, currentDepth + 1, false, playerType);
            }
            double maxEval = Double.NEGATIVE_INFINITY;
            for (MakeMoveEvent move : possibleMoves) {
                GameState newState = root.getCopy();
                newState.makeMove(move);
                double eval = minMax(newState, currentDepth + 1, true, playerType);
                maxEval = Math.max(maxEval, eval);
            }

            return maxEval;
        } else {
            if (possibleMoves.isEmpty()) {
                GameState newGameState = root.getCopy();
                newGameState.changeCurrentPlayer();
                return minMax(newGameState, currentDepth + 1, true, playerType);
            }
            double minEval = Double.POSITIVE_INFINITY;
            for (MakeMoveEvent move : possibleMoves) {
                GameState newState = root.getCopy();
                newState.makeMove(move);
                double eval = minMax(newState, currentDepth + 1, false, playerType);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    @SneakyThrows
    public MakeMoveEvent findBestMove(GameState gameState) {
        PlayerType playerType = gameState.getCurrentPlayer();
        double bestValue = Double.NEGATIVE_INFINITY;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        MakeMoveEvent bestMove = null;

        List<MakeMoveEvent> possibleMoves = gameState.getPossibleMoves();

        for (MakeMoveEvent move : possibleMoves) {
            GameState newState = gameState.getCopy();
            newState.makeMove(move);
            double moveValue = minMax(newState, 0, true, playerType);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
            alpha = Math.max(alpha, bestValue);
        }
        return bestMove;
    }




}
