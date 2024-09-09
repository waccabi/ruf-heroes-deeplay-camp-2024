package org.example;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BotFarm {
  public static final ExecutorService addPlayers = Executors.newSingleThreadExecutor();
  public static final ExecutorService players = Executors.newFixedThreadPool(8);
  private static final int port = 7070;


  public static void run(){
    try(ServerSocket serverSocket = new ServerSocket(port)){
    log.info("Server run");
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(5000);

        var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        addPlayers.execute(new CreatePlayer(socket, in, out));
      }
    } catch (IOException e) {
        throw new RuntimeException(e);
    } finally{
      addPlayers.shutdownNow();
    }
  }

  public static void main(String[] args){
    try{
      BotFarm.run();
    }catch(Exception e){
      log.error(e.getMessage());
    }
  }

}
