package io.deeplay.camp.botfarm;

import io.deeplay.camp.botfarm.bots.Bot;
import io.deeplay.camp.botfarm.bots.MinMaxBotAB;
import io.deeplay.camp.botfarm.bots.RandomBot;
import io.deeplay.camp.game.Game;
import io.deeplay.camp.game.entities.Unit;
import io.deeplay.camp.game.entities.UnitType;
import io.deeplay.camp.game.events.ChangePlayerEvent;
import io.deeplay.camp.game.events.GiveUpEvent;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameStage;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;
import java.io.IOException;


public class BotFight extends Thread {
  private final int countGame;
  private final int timeSkeep = 0;
  Game game;
  GameAnalisys gameAnalisys;
  Bot botFirst;
  Bot botSecond;
  boolean consoleOut = true;
  boolean outInfoGame;
  String separator = System.lineSeparator();
  int fightId;
  Thread threadFight;

  public BotFight(Bot botFirst, Bot botSecond, int countGame, boolean infoGame)
      throws IOException {
    this.botFirst = botFirst;
    this.botSecond = botSecond;
    this.countGame = countGame;
    fightId = (int) (100000 + Math.random() * 999999);
    gameAnalisys = new GameAnalisys(countGame, fightId);
    this.outInfoGame = infoGame;
    threadFight = new Thread(this);

    threadFight.start();
  }

  @Override
  public void run() {
    try {
      playGames();
      threadFight.interrupt();
    } catch (GameException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void playGames() throws GameException, InterruptedException, IOException {
    for (int gameCount = 0; gameCount < countGame; gameCount++) {

      game = new Game();
      //game.getGameState().setDefaultPlacement();
      executePlace(game.getGameState(), gameCount);
      game.getGameState().changeCurrentPlayer();
      executePlace(game.getGameState(), gameCount);
      game.getGameState().changeCurrentPlayer();
      gameAnalisys.setCurrentBoard(game.getGameState().getCurrentBoard().getUnits());

      while (game.getGameState().getGameStage() != GameStage.ENDED) {
        executeMove(game.getGameState(), gameCount);
        game.changePlayer(new ChangePlayerEvent(game.getGameState().getCurrentPlayer()));
        executeMove(game.getGameState(), gameCount);
        game.changePlayer(new ChangePlayerEvent(game.getGameState().getCurrentPlayer()));
      }
      System.out.println(game.getGameState().getWinner());
      gameAnalisys.reviewGame(game.getGameState(), gameCount);
      game = null;
    }

    if (outInfoGame) {
      gameAnalisys.outputInfo();
    }
  }

  public void executeMove(GameState gameState, int countGame)
      throws GameException, InterruptedException {
    for (int i = 0; i < 6; i++) {
      if (gameState.getCurrentPlayer() == PlayerType.FIRST_PLAYER) {
        if (gameState.getGameStage() == GameStage.ENDED) {
          break;
        }
        long startTimer = System.currentTimeMillis();
        MakeMoveEvent event = botFirst.generateMakeMoveEvent(gameState);
        long endTimer = System.currentTimeMillis();
        /*if (event == null) {
          System.out.println("null");
        } else {
          System.out.println(event.getAttacker() + " " + event.getFrom() + " " + event.getTo());
        }*/
        gameAnalisys.reviewTimeMove(endTimer - startTimer, countGame);
        if (endTimer - startTimer > 500000000) {
          GiveUpEvent giveUpEvent = new GiveUpEvent(PlayerType.FIRST_PLAYER);
          gameState.giveUp(giveUpEvent);
        }
        if (event == null) {
          continue;
        }
        game.makeMove(event);
        Thread.sleep(timeSkeep);
      } else {
        if (gameState.getGameStage() == GameStage.ENDED) {
          break;
        }
        long startTimer = System.currentTimeMillis();
        MakeMoveEvent event = botSecond.generateMakeMoveEvent(gameState);
        long endTimer = System.currentTimeMillis();
/*        if (event == null) {
          System.out.println("null");
        } else {
          System.out.println(event.getAttacker() + " " + event.getFrom() + " " + event.getTo());
        }*/
        gameAnalisys.reviewTimeMove(endTimer - startTimer, countGame);
        if (endTimer - startTimer > 500000000) {
          GiveUpEvent giveUpEvent = new GiveUpEvent(PlayerType.SECOND_PLAYER);
          gameState.giveUp(giveUpEvent);
        }
        if (event == null) {
          continue;
        }
        game.makeMove(event);
        Thread.sleep(timeSkeep);
      }
    }
  }

  public void executePlace(GameState gameState, int countGame)
      throws GameException, InterruptedException {
    for (int i = 0; i < 6; i++) {
      if (gameState.getCurrentPlayer() == PlayerType.FIRST_PLAYER) {

        game.placeUnit(botFirst.generatePlaceUnitEvent(gameState));

        Thread.sleep(timeSkeep);
      } else {

        game.placeUnit(botSecond.generatePlaceUnitEvent(gameState));

        Thread.sleep(timeSkeep);
      }
    }
  }

  // Методы для отображения стринговой информации о юните
  private String outUnitIsMoved(Unit unit) {
    String result = "?";
    if (unit == null) {
      return "";
    }
    if (unit.isMoved()) {
      result = "!";
    }
    return result;
  }

  private String outUnitInfo(Unit unit) {
    String result = "?";
    if (unit == null) {
      return result = "------";
    }
    switch (unit.getUnitType()) {
      case KNIGHT -> result = "Knight" + unit.getCurrentHp();
      case ARCHER -> result = "Archer" + unit.getCurrentHp();
      case MAGE -> result = "Wizard" + unit.getCurrentHp();
      case HEALER -> result = "Healer" + unit.getCurrentHp();
      default -> result = "------";
    }
    return result;
  }

  private String outUnitMove(UnitType unitType, int fromX, int fromY, int toX, int toY) {
    if (unitType == UnitType.MAGE) {
      return "Unit Mage" + "(" + fromX + "," + fromY + ") attack all enemys units";
    } else {
      String action;
      if (unitType != UnitType.HEALER) {
        action = " attack ";
      } else {
        action = " heal ";
      }
      return "Unit "
          + unitType.name()
          + "("
          + fromX
          + ","
          + fromY
          + ")"
          + action
          + game.getGameState().getCurrentBoard().getUnit(toX, toY).getUnitType().name()
          + "("
          + toX
          + ","
          + toY
          + ")";
    }
  }
}
