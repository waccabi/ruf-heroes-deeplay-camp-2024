package io.deeplay.camp.botfarm.bots.max_MinMax;

import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;

import java.io.IOException;

public interface UtilityFunction {
    double getUtility(GameState gameState, PlayerType playerType) throws IOException, InterruptedException, GameException;
}
