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

import java.util.List;

@Service(GameAction.CHANGE_CARD)
public class ChangeCardActionImpl implements GameAction {

    @Autowired
    private CardGenerator cardGenerator;

    @Override
    public Board doAction(Board state) {
        String fromPlayerId = state.getAction().getFromPlayer();
        String fromCardIdUI = state.getAction().getFromCard();
        List<Player> players = state.getPlayers();
        Player fromPlayer = getPlayerById(fromPlayerId, players);
        Card fromCard = getAvailableCardById(fromCardIdUI, fromPlayer);
        fromPlayer.getAvailableCards().remove(fromCard);
        boolean needGetCard = false;
        CardType cardType;
        do {
            cardType = cardGenerator.getRandomCardType();
            boolean isOnePerPlayerCard = CardUtils.isOnePerPlayerCard(cardType.getId());
            boolean isCardAlreadyPresent = CardUtils.hasMoreThanOneCardPerPlayer(fromPlayer.getAvailableCards(), cardType.getId());
            if (isOnePerPlayerCard && isCardAlreadyPresent) {
                needGetCard = true;
            }
        } while (needGetCard);
        Card card = new Card(cardType);
        fromPlayer.getAvailableCards().add(card);
        fromPlayer.setChangeCardAmount(fromPlayer.getChangeCardAmount() - 1);
        state.getAction().setName(GameAction.CHANGE_CARD_FINISHED);
        return state;
    }
}
