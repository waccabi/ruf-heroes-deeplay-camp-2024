package io.deeplay.camp.botfarm;

import io.deeplay.camp.botfarm.bots.Bot;
import io.deeplay.camp.botfarm.bots.BotGames;
import io.deeplay.camp.botfarm.bots.RandomBot;
import io.deeplay.camp.botfarm.bots.max_MinMax.ResultFunction;
import io.deeplay.camp.botfarm.bots.max_MinMax.TreeBuilder;
import io.deeplay.camp.game.Game;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TreeBuilderTest {
  Game game;
  Bot botFirst;
  Bot botSecond;
  GameState gameState;
  static String path =
          "C:\\Users\\Maksim\\IdeaProjects\\ruf-heroes-deeplay-camp-2024\\botfarm\\src\\main\\java\\io\\deeplay\\camp\\botfarm";

  @BeforeEach
  public void setUp() throws IOException {
    game = new Game();
    this.gameState = game.getGameState();
  }

  @Test
  void testTreeBuilderMaxDepth() {
    gameState.setDefaultPlacement();
    // first
    game.getGameState().getBoard().getUnit(0, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 1).setAccuracy(100);
    game.getGameState().getBoard().getUnit(1, 1).setCurrentHp(100);
    // second
    game.getGameState().getBoard().getUnit(0, 2).setCurrentHp(5);
    game.getGameState().getBoard().getUnit(0, 2).setAccuracy(-100);
    game.getGameState().getBoard().getUnit(2, 2).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 2).setCurrentHp(10);
    game.getGameState().getBoard().getUnit(1, 2).setAccuracy(-100);

    TreeBuilder.TreeStats treeStats =  TreeBuilder.buildGameTree(game.getGameState());
    Assertions.assertEquals(treeStats.getMaxDepth(),5);
    Assertions.assertEquals(treeStats.getNumTerminalNodes(),5);
    Assertions.assertEquals(treeStats.getNumNodes(),21);
  }

  @Test
  void testTreeBuilderMaxDepth1() {
    gameState.setDefaultPlacement();
    // first
    game.getGameState().getBoard().getUnit(0, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 1).setAccuracy(100);
    game.getGameState().getBoard().getUnit(1, 1).setCurrentHp(100);
    // second
    game.getGameState().getBoard().getUnit(0, 2).setCurrentHp(5);
    game.getGameState().getBoard().getUnit(0, 2).setAccuracy(-100);
    game.getGameState().getBoard().getUnit(2, 2).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 2).setCurrentHp(5);
    game.getGameState().getBoard().getUnit(1, 2).setAccuracy(-100);


    TreeBuilder.TreeStats treeStats =  TreeBuilder.buildGameTree(game.getGameState());
    Assertions.assertEquals(treeStats.getMaxDepth(),3);
    Assertions.assertEquals(treeStats.getNumTerminalNodes(),2);
    Assertions.assertEquals(treeStats.getNumNodes(),7);

  }
  @Test
  void testTreeBuilderMaxDepth2() {
    gameState.setDefaultPlacement();
    // first
    game.getGameState().getBoard().getUnit(0, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 0).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 1).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 1).setAccuracy(100);
    game.getGameState().getBoard().getUnit(1, 1).setCurrentHp(100);
    // second
    game.getGameState().getBoard().getUnit(0, 2).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 2).setAccuracy(-100);
    game.getGameState().getBoard().getUnit(2, 2).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(0, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(2, 3).setCurrentHp(0);
    game.getGameState().getBoard().getUnit(1, 2).setCurrentHp(10);
    game.getGameState().getBoard().getUnit(1, 2).setAccuracy(-100);

    TreeBuilder.TreeStats treeStats =  TreeBuilder.buildGameTree(game.getGameState());
    Assertions.assertEquals(treeStats.getMaxDepth(),0);
    Assertions.assertEquals(treeStats.getNumTerminalNodes(),1);
    Assertions.assertEquals(treeStats.getNumNodes(),4);

  }

  @Test
  void testResultFunction1() {
    game.getGameState().setDefaultPlacement();
    ResultFunction resultFunction = new ResultFunction();
    gameState.getBoard().getUnit(1, 2).setCurrentHp(0);
    gameState.changeCurrentPlayer();
    double a = resultFunction.getUtility(gameState, PlayerType.SECOND_PLAYER);
    Assertions.assertEquals(a, -15);
    gameState.changeCurrentPlayer();
    double b = resultFunction.getUtility(gameState, PlayerType.FIRST_PLAYER);
    Assertions.assertEquals(b, 15);
  }
  @Test
  void platGameBotsWithPlacement() throws InterruptedException, GameException {
    game.getGameState().setDefaultPlacement();
    this.botFirst = new RandomBot();
    this.botSecond = new RandomBot();
    for(int i = 0; i < 10; i++) {
      BotGames botGames = new BotGames(botFirst, botSecond, gameState);

    }
  }
  @Test
  void platGameBotsWithOutPlacement() throws InterruptedException, GameException {
    this.botFirst = new RandomBot();
    this.botSecond = new RandomBot();
    for(int i = 0; i < 10; i++) {
      BotGames botGames = new BotGames(botFirst, botSecond, gameState);

    }
  }

}
