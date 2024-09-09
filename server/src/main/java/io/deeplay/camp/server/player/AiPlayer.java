package io.deeplay.camp.server.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.deeplay.camp.core.dto.JsonConverter;
import io.deeplay.camp.core.dto.client.ClientDto;
import io.deeplay.camp.core.dto.client.game.ChangePlayerDto;
import io.deeplay.camp.core.dto.client.game.MakeMoveDto;
import io.deeplay.camp.core.dto.client.game.PlaceUnitDto;
import io.deeplay.camp.core.dto.client.party.CreateGamePartyDto;
import io.deeplay.camp.core.dto.server.GameStateDto;
import io.deeplay.camp.core.dto.server.ServerDto;
import io.deeplay.camp.game.events.MakeMoveEvent;
import io.deeplay.camp.game.mechanics.GameState;
import io.deeplay.camp.game.mechanics.PlayerType;
import io.deeplay.camp.server.GameParty;
import io.deeplay.camp.server.InfluxDBService;
import io.deeplay.camp.server.exceptions.GamePartyException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AiPlayer extends Player {
  private String bot;
  private final GameParty gameParty;
  private final InfluxDBService influxDBService = new InfluxDBService();
  private Socket botfarmSocket = new Socket("localhost", 7070);
  private final BufferedReader in =
      new BufferedReader(new InputStreamReader(botfarmSocket.getInputStream()));
  private final BufferedWriter out =
      new BufferedWriter(new OutputStreamWriter(botfarmSocket.getOutputStream()));

  public AiPlayer(PlayerType playerType, GameParty gameParty, CreateGamePartyDto createGamePartyDto) throws IOException {
    super(playerType);
    this.bot = createGamePartyDto.getBot();
    this.gameParty = gameParty ;
    sendCreateRequest(createGamePartyDto);
  }

  public void sendCreateRequest(CreateGamePartyDto createGamePartyDto) throws IOException {
    try{
      String clientDtoJson = JsonConverter.serialize(createGamePartyDto);
      out.write(clientDtoJson);
      out.newLine();
      out.flush();
    }catch(JsonProcessingException e){
      log.error(e.getMessage());
    }
  }

  public void sendRequest(GameStateDto gameStateDto) throws IOException {
    try {
      String serverDtoJson = JsonConverter.serialize(gameStateDto);
      out.write(serverDtoJson);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      log.error("Error sending response", e);
    }
  }

  public String readRequest() throws IOException {
    try{
      return in.readLine();
    }catch (IOException e){
      log.error("Error reading response", e);
      return null;
    }
  }

  // TODO Тема с тем что у нас ранее бот который был втсроенный рекурсивно проходился по и делал
  // почтановки и т.д. работаяя с бордой из
  // gameparty, с ней же работает и человек но бот делает ход и мы принемаем его ход за правильный
  // без проверки и присуждаем gamstete отданый ботом ответ

  // У нас на серве храниться в gameparty gamestate игры и мы его каждый раз меняем когда клиеинт челоек его
  // updategamestate и при обновлении он отправляется каждому клиенту в том числе и боту
  // это же тригерится и при добавлении


  //TODO GetAnswer

  @Override
  public void updateGameState(GameStateDto gameStateDto) {
    GameState gameState = gameStateDto.getGameState();
    if (gameState.getCurrentPlayer() == this.getPlayerType()) {
      switch (gameState.getGameStage()) {
        case PLACEMENT_STAGE -> {
          try {
            long startTime = System.currentTimeMillis();
            sendRequest(gameStateDto);
            String req = readRequest();
            if (req == null) {
              gameParty.processChangePlayer(new ChangePlayerDto(gameParty.getGamePartyId()));
            }
            PlaceUnitDto placeUnitDto = JsonConverter.deserialize(req, PlaceUnitDto.class);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            influxDBService.writeData("bot_response", "time_response", (double) elapsedTime);
              gameParty.processPlaceUnit(placeUnitDto);
          } catch (GamePartyException e) {
              throw new RuntimeException(e);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
        }
        case MOVEMENT_STAGE -> {
          try {
            long startTime = System.currentTimeMillis();
            sendRequest(gameStateDto);
            String req = readRequest();
            if (req == null) {
              gameParty.processChangePlayer(new ChangePlayerDto(gameParty.getGamePartyId()));
            }
            MakeMoveDto makeMoveDto = JsonConverter.deserialize(req, MakeMoveDto.class);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            influxDBService.writeData("bot_response", "time_response", (double) elapsedTime);
              gameParty.processMakeMove(makeMoveDto);
            } catch (GamePartyException e) {
              throw new RuntimeException(e);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
        }
        case ENDED -> {
          try {
            gameParty.closeParty(null);
          } catch (GamePartyException e) {
            throw new RuntimeException(e);
          }
        }
        default -> {
          System.out.println("Неопознанное состояние");
        }
      }
    }
  }

  @Override
  public boolean isBotPlayer() {
    return true;
  }
}
