package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service(GameAction.START_GAME)
public class GameInitializationActionImpl implements GameAction {

    @Override
    public Board doAction(Board state) {
        final int initialCardAmount = 5;
        Map<Integer, CardType> cardMap = new HashMap<>();
        cardMap.put(0, CardType.BAR);
        cardMap.put(1, CardType.LABORATORY);
        cardMap.put(2, CardType.MINE);
        cardMap.put(3, CardType.ROAD);
        cardMap.put(4, CardType.WASTE_RECYCLE);
        Random random = new Random();
        for (Player player : state.getPlayers()) {
            for (int i = 0; i < initialCardAmount; i++) {
                int cardIndex = random.nextInt(5);
                Card card = new Card(cardMap.get(cardIndex));
                card.setId(player.getName() + "-" + card.getId() + "-" + i);
                player.getAvailableCards().add(card);
            }
        }
        state.setRedResourceCount(state.getPlayers().size() * 5 + random.nextInt(11) + 10);
        state.getAction().setName(GameAction.START_GAME_COMPLETED);
        return state;
    }
}
