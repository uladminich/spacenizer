package com.minich.project.training.spacenizer.core.server;

import com.minich.project.training.spacenizer.core.server.formatter.BoardDecoder;
import com.minich.project.training.spacenizer.core.server.formatter.BoardEncoder;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(
        value = "/board/{id}/{name}",
        encoders = BoardEncoder.class,
        decoders = BoardDecoder.class)
public class ServerWebSocket {
    private Session session;
    private String roomId;
    private static CopyOnWriteArraySet<String> gameTokens = new CopyOnWriteArraySet<>(); // TODO move to separate component
    private static Map<String, Set<ServerWebSocket>> rooms = new ConcurrentHashMap<>(); // TODO ^
    private static Map<String, Board> boards = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) throws IOException {
        this.session = session;
        String path = session.getRequestURI().getPath();
        if (StringUtils.isNotEmpty(path)) {
            String[] pathSplitted = path.split("/");
            String id = pathSplitted[pathSplitted.length - 2];
            if (StringUtils.isNotEmpty(path)) {
                roomId = id;
                if (!gameTokens.contains(roomId)) {
                    // close connection if it's not present in room list
                    session.close();
                }
                Set<ServerWebSocket> listeners = rooms.get(roomId);
                if (Objects.isNull(listeners)) {
                    listeners = new CopyOnWriteArraySet<>();
                    listeners.add(this);
                    rooms.put(roomId, listeners);
                } else {
                    listeners.add(this);
                }
                Board board = getOrCreateBoard(roomId, name);
                broadcast(board);
            }
        }
    }

    @OnMessage
    public void onMessage(Board state) {
        broadcast(state);
    }

    @OnClose
    public void onClose(Session session) {
        rooms.get(roomId).remove(this);
    }

    private void broadcast(Board state) {
        for (ServerWebSocket listener : rooms.get(roomId)) {
            listener.sendMessage(state);
        }
    }

    private void sendMessage(Board state) {
        try {
            this.session.getBasicRemote().sendObject(state);
        }  catch (EncodeException | IOException e) {
            log.error("Error during send message to client", e);
        }
    }

    public CopyOnWriteArraySet<String> getGameTokens() {
        return gameTokens;
    }

    public void setGameTokens(CopyOnWriteArraySet<String> gameTokens) {
        this.gameTokens = gameTokens;
    }

    private Board getOrCreateBoard(String id, String name) {
        Board board = boards.get(id);
        if (null == board) {
            board = new Board();
            board.setBoardId(id);
            board.setPlayers(new ArrayList<>());
        }
        Player player = new Player();
        player.setBoardId(id);
        player.setName(name);
        board.addPlayer(player);
        board.setLastMessage("Пользователь [" + name + "] подключился");
        return board;
    }
}