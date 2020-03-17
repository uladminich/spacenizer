package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import com.minich.project.training.spacenizer.utils.CardUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;

@Service(GameAction.PLAY_CARD)
public class PlayCardActionImpl implements GameAction {

    private final BiFunction<Card, Boolean, Integer> GET_CARD_RED_PRODUCTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardRedProduction = cardType.getRedProduction();
        if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
            cardRedProduction *=  CardType.ROBOTS.getMultiplier();
        }
        return cardRedProduction;
    };

    private final BiFunction<Card, Boolean, Integer> GET_CARD_RED_CONSUMPTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardRedConsumption = cardType.getRedConsumption();
        if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
            cardRedConsumption *=  CardType.ROBOTS.getMultiplier();
        }
        return cardRedConsumption;
    };

    private final BiFunction<Card, Boolean, Integer> GET_CARD_BLUE_PRODUCTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardBlueProduction = cardType.getBlueProduction();
        if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
            cardBlueProduction *=  CardType.ROBOTS.getMultiplier();
        }
        return cardBlueProduction;
    };

    private final BiFunction<Card, Boolean, Integer> GET_CARD_BLUE_CONSUMPTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardBlueConsumption = cardType.getBlueConsumption();
        if (toPlayerHasRobots && isRobotsAffectedCards(cardType)) {
            cardBlueConsumption *=  CardType.ROBOTS.getMultiplier();
        }
        return cardBlueConsumption;
    };

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
        int totalRedProduction = getTotalResourceStat(toPlayer, toPlayerHasRobots, GET_CARD_RED_PRODUCTION);
        int totalRedConsumption = getTotalResourceStat(toPlayer, toPlayerHasRobots, GET_CARD_RED_CONSUMPTION);
        int totalBlueProduction = getTotalResourceStat(toPlayer, toPlayerHasRobots, GET_CARD_BLUE_PRODUCTION);
        int totalBlueConsumption = getTotalResourceStat(toPlayer, toPlayerHasRobots, GET_CARD_BLUE_CONSUMPTION);

        toPlayer.setRedProduction(totalRedProduction);
        toPlayer.setRedConsumption(totalRedConsumption);
        toPlayer.setBlueProduction(totalBlueProduction);
        toPlayer.setBlueConsumption(totalBlueConsumption);

        updateNegativeValueWithZero(toPlayer);

        state.getAction().setName(GameAction.PLAY_CARD_FINISHED);
        return state;
    }

    private int getTotalResourceStat(Player player, boolean isToPlayerHasRobots, BiFunction<Card, Boolean, Integer> calculateFunction) {
        return player.getActiveCards()
                .stream()
                .mapToInt(card -> calculateFunction.apply(card, isToPlayerHasRobots))
                .sum();
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

        if (player.getBlueProduction() < 0) {
            player.setBlueProduction(0);
        }

        if (player.getBlueConsumption() < 0) {
            player.setBlueConsumption(0);
        }
    }
}
