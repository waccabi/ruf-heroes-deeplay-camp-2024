package io.deeplay.camp.botfarm.bots;

import io.deeplay.camp.botfarm.bots.max_MinMax.MinMaxAlgorithmClassic;
import io.deeplay.camp.botfarm.bots.max_MinMax.ResultFunction;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.events.PlaceUnitEvent;
import io.deeplay.camp.game.mechanics.GameState;

import java.util.List;

public class MinMaxBotClassic extends Bot{
    private final MinMaxAlgorithmClassic minMaxAlgorithmClassic;


    public MinMaxBotClassic(int maxDepth) {
        this.minMaxAlgorithmClassic =new MinMaxAlgorithmClassic(maxDepth,new ResultFunction());
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
        return minMaxAlgorithmClassic.findBestMove(gameState);
    }


}
