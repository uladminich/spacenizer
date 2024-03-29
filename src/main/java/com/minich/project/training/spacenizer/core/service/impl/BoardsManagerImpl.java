package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.BoardsManager;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BoardsManagerImpl implements BoardsManager {

    private final Map<String, Board> boards = new ConcurrentHashMap<>(); // list of active boards

    @Override
    public Board getOrCreateBoard(String gameId, String userName) {
        Board board = boards.get(gameId);
        boolean isCreator = false;
        if (null == board) {
            board = new Board();
            board.setBoardId(gameId);
            board.setPlayers(new ArrayList<>());
            isCreator = true;
        }

        Player player = getOrCreatePlayer(board, gameId, userName, isCreator);
        Player globalPlayer = board.getGlobalPlayer();
        if (Objects.isNull(globalPlayer)) {
            board.setGlobalPlayer(getGlobalPlayer(gameId));
        }
        board.addPlayer(player);
        return board;
    }


    @Override
    public void addBoard(@NonNull String gameId, @NonNull Board board) {
        boards.put(gameId, board);
    }

    @Override
    public boolean isBoardPresent(String gameId) {
        return boards.containsKey(gameId);
    }

    private Player getOrCreatePlayer(Board board, String id, String name, boolean isCreator) {
        Optional<Player> optPlayer = board.getPlayers().stream()
                .filter(playerId -> playerId.getName().equals(name))
                .findAny();
        if (optPlayer.isPresent()) {
            return optPlayer.get();
        }
        Player player = new Player();
        player.setBoardId(id);
        player.setName(name);
        player.setCreator(isCreator);
        List<Card> activeCards = new ArrayList<>();
        Card card = new Card(CardType.STATION);
        card.setIdUI(player.getName() + "-" +card.getId() + "-0");
        activeCards.add(card);
        player.setActiveCards(activeCards);
        player.setChangeCardAmount(2);
        player.setAvailableCards(new ArrayList<>());
        player.setRedAmount(new Random().nextInt(2) + 4);// start amount from 4 to 6
        player.setRedConsumption(CardType.STATION.getRedConsumption());
        player.setRedProduction(CardType.STATION.getRedProduction());
        return player;
    }

    private Player getGlobalPlayer(String id) {
        Player player = new Player();
        player.setBoardId(id);
        List<Card> activeCards = new ArrayList<>();
        player.setActiveCards(activeCards);
        return player;
    }

}
