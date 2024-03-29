package com.minich.project.training.spacenizer.core.server;

import com.minich.project.training.spacenizer.config.ApplicationContextProvider;
import com.minich.project.training.spacenizer.core.server.formatter.BoardDecoder;
import com.minich.project.training.spacenizer.core.server.formatter.BoardEncoder;
import com.minich.project.training.spacenizer.core.service.BoardsManager;
import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.MockProvider;
import com.minich.project.training.spacenizer.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(
        value = "/board/{id}/{name}",
        encoders = BoardEncoder.class,
        decoders = BoardDecoder.class)
public class ServerWebSocket {
    private static final int ROOM_SIZE_LIMIT = 6;
    private static final String SLASH = "/";

    private BoardsManager boardsManager;
    private GameManager gameManager;
    private ConnectionManager connectionManager;
    private MockProvider mockProvider;

    private Session session;
    private String roomId;

    public ServerWebSocket() {
        connectionManager = ApplicationContextProvider.getApplicationContext().getBean(ConnectionManager.class);
        boardsManager = ApplicationContextProvider.getApplicationContext().getBean(BoardsManager.class);
        gameManager = ApplicationContextProvider.getApplicationContext().getBean(GameManager.class);
        mockProvider = ApplicationContextProvider.getApplicationContext().getBean(MockProvider.class);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) throws IOException {
        this.session = session;
        String path = session.getRequestURI().getPath();
        if (StringUtils.isNotEmpty(path)) {
            String[] pathSplitted = path.split(SLASH);
            String id = pathSplitted[pathSplitted.length - 2];
            if (StringUtils.isNotEmpty(path)) {
                roomId = id;
                if (!connectionManager.getGameTokens().contains(roomId)) {
                    // close connection if it's not present in room list
                    session.close();
                }
                Set<ServerWebSocket> listeners = connectionManager.getRoomById(roomId);
                if (Objects.isNull(listeners)) {
                    listeners = new CopyOnWriteArraySet<>();
                    listeners.add(this);
                    connectionManager.addRoom(roomId, listeners);
                } else {
                    if (listeners.size() > ROOM_SIZE_LIMIT) {
                        session.close();
                        return;
                    } else {
                        listeners.add(this);
                    }
                }
                Board board = getBoard(name);
                boardsManager.addBoard(roomId, board);
                broadcast(board);
            }
        }
    }

    private Board getBoard(String name) {
        boolean isMock = mockProvider.isMockId(roomId);
        boolean isGamePresent = boardsManager.isBoardPresent(roomId);
        Board board = isMock && !isGamePresent ? mockProvider.getMockGame(roomId): boardsManager.getOrCreateBoard(roomId, name);
        if (Objects.isNull(board) && isMock) {
            board = boardsManager.getOrCreateBoard(roomId, name);
        }
        return board;
    }

    @OnMessage
    public void onMessage(Board state) {
        String currentAction = state.getAction().getName();
        Board updatedState = gameManager.doAction(currentAction, state);
        broadcast(updatedState);
    }

    @OnClose
    public void onClose(Session session) {
        Set<ServerWebSocket> room = connectionManager.getRoomById(roomId);
        if (Objects.nonNull(room)) {
            room.remove(this);
            if (room.isEmpty()) {
                connectionManager.removeRoomById(roomId);
            }
        }
    }

    private void broadcast(Board state) {
        for (ServerWebSocket listener : connectionManager.getRoomById(roomId)) {
            listener.sendMessage(state);
        }
    }

    private void sendMessage(Board state) {
        try {
            this.session.getBasicRemote().sendObject(state);
        } catch (EncodeException | IOException e) {
            log.error("Error during send message to client", e);
        }
    }
}