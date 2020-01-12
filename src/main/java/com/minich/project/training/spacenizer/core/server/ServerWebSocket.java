package com.minich.project.training.spacenizer.core.server;

import com.minich.project.training.spacenizer.config.ApplicationContextProvider;
import com.minich.project.training.spacenizer.core.server.formatter.BoardDecoder;
import com.minich.project.training.spacenizer.core.server.formatter.BoardEncoder;
import com.minich.project.training.spacenizer.core.service.BoardsManager;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.socket.server.standard.SpringConfigurator;

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
    private static final int ROOM_SIZE_LIMIT = 2;

    @Autowired
    private BoardsManager boardsManager;
    @Autowired
    private ConnectionManager connectionManager;

    private Session session;
    private String roomId;

    public ServerWebSocket() {
        connectionManager = ApplicationContextProvider.getApplicationContext().getBean(ConnectionManager.class);
        boardsManager = ApplicationContextProvider.getApplicationContext().getBean(BoardsManager.class);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) throws IOException {
        this.session = session;
        String path = session.getRequestURI().getPath();
        if (StringUtils.isNotEmpty(path)) {
            String[] pathSplitted = path.split("/");
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
                    if (listeners.size() >= ROOM_SIZE_LIMIT) {
                        session.close();
                    } else {
                        listeners.add(this);
                    }
                }
                Board board = boardsManager.getOrCreateBoard(roomId, name);
                boardsManager.addBoard(roomId, board);
                broadcast(board);
            }
        }
    }

    @OnMessage
    public void onMessage(Board state) {
        if ("start".equals(state.getAction())) {
            Board updatedState = startGameInitialization(state);
            broadcast(updatedState);
            return;
        }
        broadcast(state);
    }

    @OnClose
    public void onClose(Session session) {
            connectionManager.getRoomById(roomId).remove(this);
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

    private Board startGameInitialization(Board board) {
        final int initialCardAmount = 5;
        Map<Integer, CardType> cardMap = new HashMap<>();
        cardMap.put(0, CardType.BAR);
        cardMap.put(1, CardType.LABORATORY);
        cardMap.put(2, CardType.MINE);
        cardMap.put(3, CardType.ROAD);
        cardMap.put(4, CardType.WASTE_RECYCLE);
        Random random = new Random();
        for(Player player : board.getPlayers()) {
            for (int i = 0; i < initialCardAmount; i++) {
                int cardIndex = random.nextInt(5);
                Card card = new Card(cardMap.get(cardIndex));
                card.setId(player.getName() + "-" +card.getId() + "-" + i);
                player.getAvailableCards().add(card);
            }
        }
        board.setRedResourceCount(board.getPlayers().size() * 5 + random.nextInt(11) + 10);
        board.setAction("start_completed");
        return board;
    }
}