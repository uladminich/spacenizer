package com.minich.project.training.spacenizer.core.server;

import com.minich.project.training.spacenizer.core.server.formatter.BoardDecoder;
import com.minich.project.training.spacenizer.core.server.formatter.BoardEncoder;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(
        value = "/board/{id}/{name}",
        encoders = BoardEncoder.class,
        decoders = BoardDecoder.class)
public class ServerWebSocket {
    private static final int ROOM_SIZE_LIMIT = 2;
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
                    if (listeners.size() >= ROOM_SIZE_LIMIT) {
                        session.close();
                    } else {
                        listeners.add(this);
                    }
                }
                Board board = getOrCreateBoard(roomId, name);
                boards.put(roomId, board);
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
        } catch (EncodeException | IOException e) {
            log.error("Error during send message to client", e);
        }
    }

    public CopyOnWriteArraySet<String> getGameTokens() {
        return gameTokens;
    }

    public void setGameTokens(CopyOnWriteArraySet<String> gameTokens) {
        ServerWebSocket.gameTokens = gameTokens;
    }

    private Board getOrCreateBoard(String id, String name) {
        Board board = boards.get(id);
        boolean isCreator = false;
        if (null == board) {
            board = new Board();
            board.setBoardId(id);
            board.setPlayers(new ArrayList<>());
            isCreator = true;
        }
        Player player = initPlayer(id, name, isCreator);
        board.addPlayer(player);
        return board;
    }

    private Player initPlayer(String id, String name, boolean isCreator) {
        Player player = new Player();
        player.setBoardId(id);
        player.setName(name);
        player.setCreator(isCreator);
        List<Card> activeCards = new ArrayList<>();
        Card card = new Card(CardType.STATION);
        card.setCardId(player.getName() + "-" +card.getName());
        activeCards.add(card);
        player.setActiveCards(activeCards);
        player.setAvailableCards(new ArrayList<>());
        player.setRedAmount(0);
        player.setRedConsumption(CardType.STATION.getRedConsumption());
        player.setRedProduction(CardType.STATION.getRedProduction());
        return player;
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
                card.setCardId(player.getName() + "-" +card.getName());
                player.getAvailableCards().add(card);
            }
        }
        board.setRedResourceCount(board.getPlayers().size() * 5 + random.nextInt(11) + 10);
        board.setAction("start_completed");
        return board;
    }
}