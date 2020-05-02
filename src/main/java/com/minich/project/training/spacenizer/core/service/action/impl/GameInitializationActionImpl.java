package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.CardGenerator;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import com.minich.project.training.spacenizer.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;

@Service(GameAction.START_GAME)
public class GameInitializationActionImpl implements GameAction {
    private static final int INITIAL_AVAILABLE_CARD_AMOUNT = 6;
    private static final Random RANDOM = new Random();

    @Autowired
    private CardGenerator cardGenerator;

    @Override
    public Board doAction(Board state) {
        for (Player player : state.getPlayers()) {
            for (int i = 0; i < INITIAL_AVAILABLE_CARD_AMOUNT; i++) {
                CardType cardForPlayer = cardGenerator.getRandomCardType();
                boolean isOnePerPlayerCard = CardUtils.isOnePerPlayerCard(cardForPlayer.getId());
                boolean isCardAlreadyPresent = CardUtils.hasMoreThanOneCardPerPlayer(player.getAvailableCards(), cardForPlayer.getId());
                if (isOnePerPlayerCard && isCardAlreadyPresent) {
                    i--;
                    continue;
                }
                Card card = new Card(cardForPlayer);
                card.setIdUI(player.getName() + DASH + cardForPlayer.getId() + DASH + i);
                player.getAvailableCards().add(card);
            }
        }
        state.setRedResourceCount(state.getPlayers().size() * 6 + RANDOM.nextInt(20) + 25); //TODO improve formula
        state.setBlueResourceCount(state.getPlayers().size() * 4 + RANDOM.nextInt(11) + 15); //TODO improve formula
        state.setTotalResourceCount(state.getBlueResourceCount( ) + state.getRedResourceCount());
        // shuffle player before game as first turn will be for the first player
        Collections.shuffle(state.getPlayers());
        state.getPlayers().get(0).setActiveTurn(true);
        state.setFirstPlayerId(state.getPlayers().get(0).getName());
        state.getAction().setName(GameAction.START_GAME_COMPLETED);
        return state;
    }

}
