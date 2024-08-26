package io.deeplay.camp.botfarm;


import io.deeplay.camp.botfarm.bots.Bot;
import io.deeplay.camp.botfarm.bots.MinMaxBotAB;
import io.deeplay.camp.botfarm.bots.MinMaxBotClassic;
import io.deeplay.camp.botfarm.bots.RandomBot;
import io.deeplay.camp.game.mechanics.PlayerType;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  static String path =
      "C:\\Users\\Maksim\\IdeaProjects\\ruf-heroes-deeplay-camp-2024\\botfarm\\src\\main\\java\\io\\deeplay\\camp\\botfarm";

  public static void main(String[] args) throws IOException {

    deleteFilesForPathByPrefix(path, "resultgame");

    Bot bot1 = new MinMaxBotClassic(1);
    Bot bot2 = new RandomBot();
    for(int i = 0; i<1;i++){
      BotFight fight = new BotFight(bot1 , bot2, 10, true);
    }
  }

  public static boolean deleteFilesForPathByPrefix(final String path, final String prefix) {
    boolean success = true;
    try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(Paths.get(path), prefix + "*")) {
      for (final Path newDirectoryStreamItem : newDirectoryStream) {
        Files.delete(newDirectoryStreamItem);
      }
    } catch (final Exception e) {
      success = false;
      e.printStackTrace();
    }
    return success;
  }
}
