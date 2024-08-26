package io.deeplay.camp.botfarm.bots;

import io.deeplay.camp.game.events.GiveUpEvent;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameStage;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;

public class BotGames {
  Bot botFirst;
  Bot botSecond;
  GameState gameState;

  public BotGames(Bot botFirst, Bot botSecond, GameState gameState) {
    this.botFirst = botFirst;
    this.botSecond = botSecond;
    this.gameState = gameState.getCopy();
  }

  public PlayerType playBotGames(GameStage gameStage) throws InterruptedException, GameException {
    if (gameState.getGameStage() == GameStage.PLACEMENT_STAGE) {
      executePlace();
      gameState.changeCurrentPlayer();
      executePlace();
      gameState.changeCurrentPlayer();
      while (gameState.getGameStage() != GameStage.ENDED) {
        executeMove();
        gameState.changeCurrentPlayer();
        executeMove();
        gameState.changeCurrentPlayer();
      }
    } else if (gameState.getGameStage() == GameStage.MOVEMENT_STAGE) {
      while (gameState.getGameStage() != GameStage.ENDED) {
        executeMove();
        gameState.changeCurrentPlayer();
        executeMove();
        gameState.changeCurrentPlayer();
      }
    }
    return gameState.getWinner();
  }

  public void executePlace() throws GameException, InterruptedException {
    for (int i = 0; i < 6; i++) {
      if (gameState.getCurrentPlayer() == PlayerType.FIRST_PLAYER) {
        gameState.makePlacement(botFirst.generatePlaceUnitEvent(gameState));
      } else {
        gameState.makePlacement(botSecond.generatePlaceUnitEvent(gameState));
      }
    }
  }

  public void executeMove() throws GameException {
    for (int i = 0; i < 6; i++) {
      if (gameState.getCurrentPlayer() == PlayerType.FIRST_PLAYER) {
        if (gameState.getGameStage() == GameStage.ENDED) {
          break;
        }
        long startTimer = System.currentTimeMillis();
        MakeMoveEvent event = botFirst.generateMakeMoveEvent(gameState);
        long endTimer = System.currentTimeMillis();
        if (endTimer - startTimer > 500000000) {
          GiveUpEvent giveUpEvent = new GiveUpEvent(PlayerType.FIRST_PLAYER);
          gameState.giveUp(giveUpEvent);
        }
        if (event == null) {
          continue;
        }
        gameState.makeMove(event);
      } else {
        if (gameState.getGameStage() == GameStage.ENDED) {
          break;
        }
        long startTimer = System.currentTimeMillis();
        MakeMoveEvent event = botSecond.generateMakeMoveEvent(gameState);
        long endTimer = System.currentTimeMillis();
        if (endTimer - startTimer > 500000000) {
          GiveUpEvent giveUpEvent = new GiveUpEvent(PlayerType.SECOND_PLAYER);
          gameState.giveUp(giveUpEvent);
        }
        if (event == null) {
          continue;
        }
        gameState.makeMove(event);
      }
    }
  }
}
