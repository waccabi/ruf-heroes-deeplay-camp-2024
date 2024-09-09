package io.deeplay.camp.botfarm.bots;

import io.deeplay.camp.botfarm.bots.max_MinMax.MinMaxAlgorithmAB;
import io.deeplay.camp.botfarm.bots.max_MinMax.UtilityFunction;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.events.PlaceUnitEvent;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;
import java.util.List;

public class MinMaxBotAB extends Bot {
    private final MinMaxAlgorithmAB minMaxAlgorithmAB;


    public MinMaxBotAB(int maxDepth, UtilityFunction utilityFunction){
        this.minMaxAlgorithmAB = new MinMaxAlgorithmAB(maxDepth,utilityFunction);
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

    private PlaceUnitEvent generateFirstPlayerPlaceUnitEvent(GameState gameState){
        List<PlaceUnitEvent> possiblePlaces = gameState.getPossiblePlaces();
        for(PlaceUnitEvent p : possiblePlaces){
            switch (p.getUnit().getUnitType()){
                case KNIGHT -> {
                    if (p.getRows() == 1){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case ARCHER -> {
                    if (p.getRows() == 0){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case MAGE -> {
                    if (p.getRows() == 0){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case HEALER -> {
                    if (p.getRows() == 0){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case null, default -> {
                    return null;
                }
            }
        }
        return null;
    }


    private PlaceUnitEvent generateSceondPlayerPlaceUnitEvent(GameState gameState){
        List<PlaceUnitEvent> possiblePlaces = gameState.getPossiblePlaces();
        for(PlaceUnitEvent p : possiblePlaces){
            switch (p.getUnit().getUnitType()){
                case KNIGHT -> {
                    if (p.getRows() == 2){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case ARCHER -> {
                    if (p.getRows() == 3){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case MAGE -> {
                    if (p.getRows() == 3){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case HEALER -> {
                    if (p.getRows() == 3){
                        return p;
                    }
                    else {
                        continue;
                    }
                }
                case null, default -> {
                    return null;
                }
            }
        }
        return null;
    }

}
