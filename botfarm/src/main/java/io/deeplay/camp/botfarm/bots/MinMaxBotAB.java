package io.deeplay.camp.botfarm.bots;

import io.deeplay.camp.botfarm.bots.max_MinMax.MKFunction;
import io.deeplay.camp.botfarm.bots.max_MinMax.MinMaxAlgorithmAB;
import io.deeplay.camp.botfarm.bots.max_MinMax.ResultFunction;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.events.PlaceUnitEvent;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;

import java.util.List;

public class MinMaxBotAB extends Bot {
    private final MinMaxAlgorithmAB minMaxAlgorithmAB;


    public MinMaxBotAB(int maxDepth) {
    this.minMaxAlgorithmAB = new MinMaxAlgorithmAB(maxDepth, new ResultFunction());
    }

    @Override
    public PlaceUnitEvent generatePlaceUnitEvent(GameState gameState){
        List<PlaceUnitEvent> placeUnitEvents = gameState.getPossiblePlaces();
        if(!placeUnitEvents.isEmpty()){
            return placeUnitEvents.get((int)(Math.random()*placeUnitEvents.size()));
        }
        else{
            return null;
        }
    }
    @Override
    public MakeMoveEvent generateMakeMoveEvent(GameState gameState) {
        return minMaxAlgorithmAB.findBestMove(gameState);
    }

}
