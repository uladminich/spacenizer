package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.BoardsManager;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Player player = initPlayer(gameId, userName, isCreator);
        board.addPlayer(player);
        return board;
    }


    @Override
    public void addBoard(@NonNull String gameId, @NonNull Board board) {
        boards.put(gameId, board);
    }

    private Player initPlayer(String id, String name, boolean isCreator) {
        Player player = new Player();
        player.setBoardId(id);
        player.setName(name);
        player.setCreator(isCreator);
        List<Card> activeCards = new ArrayList<>();
        Card card = new Card(CardType.STATION);
        card.setId(player.getName() + "-" +card.getId() + "-0");
        activeCards.add(card);
        player.setActiveCards(activeCards);
        player.setAvailableCards(new ArrayList<>());
        player.setRedAmount(0);
        player.setRedConsumption(CardType.STATION.getRedConsumption());
        player.setRedProduction(CardType.STATION.getRedProduction());
        return player;
    }

}
