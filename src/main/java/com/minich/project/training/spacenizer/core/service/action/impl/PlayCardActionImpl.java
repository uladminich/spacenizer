package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import com.minich.project.training.spacenizer.utils.CardUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(GameAction.PLAY_CARD)
public class PlayCardActionImpl implements GameAction {

    @Override
    public Board doAction(Board state) {
        String fromPlayerId = state.getAction().getFromPlayer();
        String toPlayerId = state.getAction().getToPlayer();
        String fromCardIdUI = state.getAction().getFromCard();
//        String toCardId = state.getAction().getFromCard(); TODO if card play to another card

        List<Player> players = state.getPlayers();
        Player fromPlayer = getPlayerById(fromPlayerId, players);
        Player toPlayer = fromPlayerId.equals(toPlayerId) ? fromPlayer : getPlayerById(toPlayerId, players);

        Card fromCard = getAvailableCardById(fromCardIdUI, fromPlayer);

        fromPlayer.getAvailableCards().remove(fromCard);
        toPlayer.getActiveCards().add(fromCard);

        boolean toPlayerHasRobots = CardUtils.isPlayerHasActiveCard(CardType.ROBOTS.getId(), toPlayer);
        int totalRedProduction = toPlayer.getActiveCards().stream()
                .mapToInt(card -> {
                    CardType cardType = CardUtils.getCardTypeById(card.getId());
                    int cardRedProduction = cardType.getRedProduction();
                    if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
                        cardRedProduction *=  CardType.ROBOTS.getMultiplier();
                    }
                    return cardRedProduction;
                }).sum();

        int totalRedConsumption = toPlayer.getActiveCards().stream()
                .mapToInt(card -> {
                    CardType cardType = CardUtils.getCardTypeById(card.getId());
                    int cardRedConsumption = cardType.getRedConsumption();
                    if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
                        cardRedConsumption *=  CardType.ROBOTS.getMultiplier();
                    }
                    return cardRedConsumption;
                }).sum();

        toPlayer.setRedProduction(totalRedProduction);
        toPlayer.setRedConsumption(totalRedConsumption);

        updateNegativeValueWithZero(toPlayer);

        state.getAction().setName(GameAction.PLAY_CARD_FINISHED);
        return state;
    }

    private boolean isRobotsAffectedCards(CardType cardType) {
        return cardType == CardType.MINE
                || cardType == CardType.WASTE_RECYCLE
                || cardType == CardType.LABORATORY
                || cardType == CardType.ROAD;
    }

    private Player getPlayerById(String id, List<Player> players) {
        return players.stream()
                .filter(p -> id.equals(p.getName()))
                .findFirst()
                .orElse(null);
    }

    private Card getAvailableCardById(String idUI, Player player) {
        return player.getAvailableCards()
                .stream()
                .filter(c -> c.getIdUI().equals(idUI))
                .findFirst()
                .orElse(null);
    }

    private void updateNegativeValueWithZero(Player player){
        if (player.getRedProduction() < 0) {
            player.setRedProduction(0);
        }

        if (player.getRedConsumption() < 0) {
            player.setRedConsumption(0);
        }
    }
}
