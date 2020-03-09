package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service(GameAction.START_GAME)
public class GameInitializationActionImpl implements GameAction {

    private static final int INITIAL_AVAILABLE_CARD_AMOUNT = 5;
    private static final int RANDOM_CARD_INDEX_MAX = 8;
    private static final List<CardType> ONE_CARD_PER_PLAYER_LIST = new ArrayList<>();
    static {
        ONE_CARD_PER_PLAYER_LIST.add(CardType.ADVERSE_TERRAIN);
        ONE_CARD_PER_PLAYER_LIST.add(CardType.NANO_TECHNOLOGIES);
        ONE_CARD_PER_PLAYER_LIST.add(CardType.ROBOTS);
        ONE_CARD_PER_PLAYER_LIST.add(CardType.SECURITY_GUARDS);
        ONE_CARD_PER_PLAYER_LIST.add(CardType.BARRACK);
    }

    @Override
    public Board doAction(Board state) {
        // TODO rewrite init random
        Map<Integer, CardType> cardMap = new HashMap<>();
        cardMap.put(0, CardType.BAR);
        cardMap.put(1, CardType.LABORATORY);
        cardMap.put(2, CardType.MINE);
        cardMap.put(3, CardType.ROAD);
        cardMap.put(4, CardType.WASTE_RECYCLE);
        cardMap.put(5, CardType.NANO_TECHNOLOGIES);
        cardMap.put(6, CardType.ADVERSE_TERRAIN);
        cardMap.put(7, CardType.ROBOTS);
        Random random = new Random();
        for (Player player : state.getPlayers()) {
            for (int i = 0; i < INITIAL_AVAILABLE_CARD_AMOUNT; i++) {
                int cardIndex = random.nextInt(RANDOM_CARD_INDEX_MAX);
                CardType cardForPlayer = cardMap.get(cardIndex);
                if (ONE_CARD_PER_PLAYER_LIST.contains(cardForPlayer) && hasOneCardPerPlayer(player.getAvailableCards(), cardForPlayer)) {
                    i--;
                    continue;
                }
                Card card = new Card(cardForPlayer);
                card.setIdUI(player.getName() + "-" + cardForPlayer.getId() + "-" + i);
                player.getAvailableCards().add(card);
            }
        }
        state.setRedResourceCount(state.getPlayers().size() * 5 + random.nextInt(11) + 10); //TODO improve formula
        state.setBlueResourceCount(state.getPlayers().size() * 5 + random.nextInt(11) + 10); //TODO improve formula

        // shuffle player before game as first turn will be for the first player
        // Collections.shuffle(state.getPlayers()); some UI issues
        state.getPlayers().get(0).setActiveTurn(true);
        state.setFirstPlayerId(state.getPlayers().get(0).getName());
        state.getAction().setName(GameAction.START_GAME_COMPLETED);
        return state;
    }

    private boolean hasOneCardPerPlayer(List<Card> availableCards, CardType cardForPlayer) {
        return availableCards.stream()
                .anyMatch(card -> card.getName().equals(cardForPlayer.getName()));
    }
}
