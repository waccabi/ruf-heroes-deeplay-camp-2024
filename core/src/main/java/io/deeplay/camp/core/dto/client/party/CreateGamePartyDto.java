package io.deeplay.camp.core.dto.client.party;

import io.deeplay.camp.core.dto.GameType;
import io.deeplay.camp.core.dto.client.ClientDto;
import io.deeplay.camp.core.dto.client.ClientDtoType;
import io.deeplay.camp.game.mechanics.PlayerType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
public class CreateGamePartyDto extends ClientDto {
  private GameType gameType;
  private PlayerType player;
  private String bot;
  private UUID gamePartyId;

  public CreateGamePartyDto(GameType gameType) {
    super(ClientDtoType.CREATE_PARTY);
    this.gameType = gameType;
  }

  public CreateGamePartyDto(GameType gameType, PlayerType playerType, String bot) {
    super(ClientDtoType.CREATE_PARTY);
    this.gameType = gameType;
    this.player = playerType;
    this.bot = bot;
  }

}
