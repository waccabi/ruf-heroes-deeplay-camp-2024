package io.deeplay.camp.botfarm.bots.max_MinMax;

import io.deeplay.camp.botfarm.bots.BotGames;
import io.deeplay.camp.botfarm.bots.RandomBot;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;

public class MKFunction implements UtilityFunction{

    @Override
    public double getUtility(GameState gameState, PlayerType playerType) throws InterruptedException, GameException {
        int playersWin = 0;
        int totalGames = 10;

        for(int i = 0; i < totalGames; i++) {
            BotGames botGames = new BotGames(new RandomBot(),new RandomBot(),gameState);
            PlayerType winer = botGames.playBotGames(gameState.getGameStage());
            if (winer == playerType){
                playersWin++;
            }
        }
        return (double) playersWin / totalGames;
    }
}
