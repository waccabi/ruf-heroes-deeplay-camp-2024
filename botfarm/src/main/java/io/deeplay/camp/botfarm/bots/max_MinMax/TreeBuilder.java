package io.deeplay.camp.botfarm.bots.max_MinMax;

import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.mechanics.GameStage;
import io.deeplay.camp.game.mechanics.GameState;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;


@NoArgsConstructor
public class TreeBuilder {

  private static TreeStats stats = new TreeStats();

  public static TreeStats buildGameTree(final GameState root) {
    stats.workTimeMs = System.currentTimeMillis();
    buildTreeRecursive(root, 0);
    stats.workTimeMs = System.currentTimeMillis() - stats.workTimeMs;

    int numNonTerminalNodes = stats.numNodes - stats.numTerminalNodes;
    stats.coefBranch = (numNonTerminalNodes > 0) ? ((double) stats.totalChildren / numNonTerminalNodes) : 0.0;

    System.out.println("Total number of nodes: " + stats.numNodes);
    System.out.println("Total number of terminal nodes: " + stats.numTerminalNodes);
    System.out.println("Max tree depth reached: " + stats.maxDepth);
    System.out.println("Average branching factor: " + stats.coefBranch);
    System.out.println("Time taken (ms): " + stats.workTimeMs);
    return stats;
  }

  @SneakyThrows
  private static void buildTreeRecursive(GameState root, int currentDepth) {

    List<MakeMoveEvent> makeMoveEvents = root.getPossibleMoves();
    stats.numNodes++;
    stats.maxDepth = Math.max(stats.maxDepth, stats.compareDepth);
    if (currentDepth == stats.endDepth) {
      return;
    }

    if (makeMoveEvents.isEmpty()) {
      if (root.getGameStage() == GameStage.ENDED) {
        stats.numTerminalNodes++;
        stats.compareDepth = currentDepth;
      } else {
        stats.totalChildren += makeMoveEvents.size();
        GameState nodeGameState = root.getCopy();
        nodeGameState.changeCurrentPlayer();
        buildTreeRecursive(nodeGameState, currentDepth + 1);
      }
    } else {
      stats.totalChildren += makeMoveEvents.size();
      for (MakeMoveEvent makeMoveEvent : makeMoveEvents) {
        GameState nodeGameState = root.getCopy();
        nodeGameState.makeMove(makeMoveEvent);
        nodeGameState.changeCurrentPlayer();
        buildTreeRecursive(nodeGameState, currentDepth + 1);
      }
    }
  }
@Getter
  public static class TreeStats {
    int numNodes;
    int numTerminalNodes;
    int maxDepth;
    int compareDepth;
    int minDepth = Integer.MAX_VALUE;
    int endDepth = 120;
    int totalChildren;
    double coefBranch;
    long workTimeMs;
  }
}

