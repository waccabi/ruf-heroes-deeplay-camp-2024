package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.deeplay.camp.botfarm.bots.Bot;
import io.deeplay.camp.botfarm.bots.RandomBot;
import io.deeplay.camp.core.dto.JsonConverter;
import io.deeplay.camp.core.dto.client.ClientDto;
import io.deeplay.camp.core.dto.client.party.CreateGamePartyDto;
import io.deeplay.camp.game.exceptions.GameException;
import io.deeplay.camp.game.mechanics.PlayerType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreatePlayer implements Runnable {
  private final Socket socket;
  @Getter private final BufferedReader in;
  @Getter private final BufferedWriter out;

  public CreatePlayer(final Socket socket, BufferedReader in, BufferedWriter out) {
    this.socket = socket;
    this.in = in;
    this.out = out;
    log.info("CreatePlayer");
  }

  @Override
  public void run() {
    try {
      var request = JsonConverter.deserialize(in.readLine(), ClientDto.class);
      switch (request.getClientDtoType()) {
        case CREATE_PARTY -> BotFarm.players.execute(getGame(request));
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException | GameException e) {
      throw new RuntimeException(e);
    }
  }

  public RunGame getGame(ClientDto clientDto) throws GameException {
    CreateGamePartyDto createGamePartyDto = (CreateGamePartyDto) clientDto;
    Bot bot = createBot(createGamePartyDto.getBot());
    if (createGamePartyDto.getPlayer() == PlayerType.FIRST_PLAYER) {
      return new RunGame(bot, socket, in, out, PlayerType.SECOND_PLAYER);
    } else if (createGamePartyDto.getPlayer() == PlayerType.SECOND_PLAYER) {
      return new RunGame(bot, socket, in, out, PlayerType.FIRST_PLAYER);
    } else {
      return null;
    }
  }

  private Bot createBot(String botName) {
    Bot bot = null;
    switch (botName) {
      case "random" -> {
        bot = new RandomBot();
      }
    }
    return bot;
  }
}
