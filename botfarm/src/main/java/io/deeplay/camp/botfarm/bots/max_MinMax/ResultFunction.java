package io.deeplay.camp.botfarm.bots.max_MinMax;

import io.deeplay.camp.game.entities.Position;
import io.deeplay.camp.game.entities.Unit;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;

import java.util.List;

public class ResultFunction implements UtilityFunction {
  @Override
  public double getUtility(GameState gameState, PlayerType playerType) {
    double score = 0.0;
      score = firstPlayerResult(gameState)-secondPlayerResult(gameState);
    return score;
  }

  private double firstPlayerResult(GameState gameState) {
    double score = 0.0;
    List<Position> playerUnitsPosition = gameState.getBoard().firstPlayerUnits();
    score = playerUnitsPosition.size();

    for (Position p : playerUnitsPosition) {
      Unit unit = gameState.getBoard().getUnitByPosition(p);

      switch (unit.getUnitType()) {
        case KNIGHT -> {
          if (p.y() == 1) {
            score++;
          } else {
            int hpFirstLineUnits = 0;
            for (Position r : playerUnitsPosition) {
              if (r.y() == 1) {
                hpFirstLineUnits += gameState.getBoard().getUnitByPosition(r).getCurrentHp();
              }
            }
            if (hpFirstLineUnits == 0) {
              score++;
            } else {
              score -= 0.5;
            }
          }
        }
        case ARCHER, MAGE, HEALER -> {
          if (p.y() == 0) {
            score++;
          } else {
            score -= 0.5;
          }
        }
        default -> throw new IllegalStateException("Unexpected value: " + unit.getUnitType());
      }
    }
    if (gameState.getArmyFirst().isBuffed) {
      score *= 1.5;
    }
    return score;
  }

  private double secondPlayerResult(GameState gameState) {
    double score = 0.0;
    List<Position> playerUnitsPosition = gameState.getBoard().secondPlayerUnits();
    score = playerUnitsPosition.size();

    for (Position p : playerUnitsPosition) {
      Unit unit = gameState.getBoard().getUnitByPosition(p);

      switch (unit.getUnitType()) {
        case KNIGHT -> {
          if (p.y() == 2) {
            score++;
          } else {
            int hpFirstLineUnits = 0;
            for (Position r : playerUnitsPosition) {
              if (r.y() == 2) {
                hpFirstLineUnits += gameState.getBoard().getUnitByPosition(r).getCurrentHp();
              }
            }
            if (hpFirstLineUnits == 0) {
              score++;
            } else {
              score -= 0.5;
            }
          }
        }
        case ARCHER, MAGE, HEALER -> {
          if (p.y() == 3) {
            score++;
          } else {
            score -= 0.5;
          }
        }
        default -> throw new IllegalStateException("Unexpected value: " + unit.getUnitType());
      }
    }
    if (gameState.getArmySecond().isBuffed) {
      score *= 1.5;
    }
    return score;
  }
}
