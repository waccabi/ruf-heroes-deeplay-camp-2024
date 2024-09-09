package org.example;

import io.deeplay.camp.botfarm.bots.Bot;
import io.deeplay.camp.core.dto.JsonConverter;
import io.deeplay.camp.core.dto.server.GameStateDto;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.GameState;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.deeplay.camp.game.mechanics.PlayerType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RunGame implements Runnable {
  private final ExecutorService executor;
  private final Bot bot;
  private final Socket socket;
  private final BufferedReader in;
  private final BufferedWriter out;
  //private final GameState gameState;

  public RunGame(Bot bot, Socket socket, BufferedReader in, BufferedWriter out, PlayerType playerType) throws GameException {
    this.executor = Executors.newSingleThreadExecutor();
    this.bot = bot;
    this.socket = socket;
    this.in = in;
    this.out = out;
    if (playerType == PlayerType.FIRST_PLAYER){
      GameState newGameState = new GameState();
      for(int i = 0; i < 6; i++) {
        newGameState.makePlacement(bot.generatePlaceUnitEvent(newGameState));
      }
      GameStateDto gameStateDto = new GameStateDto();
    }
  }

  @Override
  public void run() {
    try {
      var serverResponseDto = in.readLine();
      var req = JsonConverter.deserialize(serverResponseDto, GameStateDto.class);
      switch (req.getGameState().getGameStage()){
          case PLACEMENT_STAGE -> {
          }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
