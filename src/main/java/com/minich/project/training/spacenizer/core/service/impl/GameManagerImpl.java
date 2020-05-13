package com.minich.project.training.spacenizer.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.PostRoundService;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import com.minich.project.training.spacenizer.utils.CardUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameManagerImpl implements GameManager {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final BiFunction<Card, Boolean, Integer> GET_CARD_RED_PRODUCTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardRedProduction = cardType.getRedProduction();
        if (toPlayerHasRobots && CardUtils.isRobotsAffectedCards(cardType)) {
            cardRedProduction *=  CardType.ROBOTS.getMultiplier();
        }
        return cardRedProduction;
    };

    private static final BiFunction<Card, Boolean, Integer> GET_CARD_RED_CONSUMPTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardRedConsumption = cardType.getRedConsumption();
        if (toPlayerHasRobots && CardUtils.isRobotsAffectedCards(cardType)) {
            cardRedConsumption *=  CardType.ROBOTS.getMultiplier();
        }
        return cardRedConsumption;
    };

    private static final BiFunction<Card, Boolean, Integer> GET_CARD_BLUE_PRODUCTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardBlueProduction = cardType.getBlueProduction();
        if (toPlayerHasRobots && CardUtils.isRobotsAffectedCards(cardType)) {
            cardBlueProduction *=  CardType.ROBOTS.getMultiplier();
        }
        return cardBlueProduction;
    };

    private static final BiFunction<Card, Boolean, Integer> GET_CARD_BLUE_CONSUMPTION = (card, toPlayerHasRobots) -> {
        CardType cardType = CardUtils.getCardTypeById(card.getId());
        int cardBlueConsumption = cardType.getBlueConsumption();
        if (toPlayerHasRobots && CardUtils.isRobotsAffectedCards(cardType)) {
            cardBlueConsumption *=  CardType.ROBOTS.getMultiplier();
        }
        return cardBlueConsumption;
    };

    @Autowired
    private PostRoundService postRoundService;

    @Autowired
    private Map<String, GameAction> actions;

    @SneakyThrows
    @Override
    public Board doAction(String currentAction, Board currentState) {
        log.info("State input: {}", MAPPER.writeValueAsString(currentState));

        GameAction action = getAction(currentAction);
        Board updatedState = action != null ? action.doAction(currentState) : currentState;

        String updatedAction = updatedState.getAction().getName();

        updatePlayerResourcesStats(updatedState);

        if (GameAction.PLAY_CARD_FINISHED.equals(updatedAction)
            || GameAction.CHANGE_CARD_FINISHED.equals(updatedAction)
            || GameAction.SKIP_TURN_FINISHED.equals(updatedAction)) {
            updatedState.setTurnPerRound(updatedState.getTurnPerRound() + 1);
        }

        if (postRoundService.isRoundFinish(updatedState)) { // TODO facade?
            postRoundService.applyOneRoundCardActions(updatedState);
            postRoundService.updatePlayerResourceAmountStored(updatedState);
            postRoundService.resetTurnsPerRound(updatedState);
            postRoundService.applySpecialGlobalCardAction(updatedState);
            postRoundService.addOneCardToPlayersIfRequired(updatedState);
        }

        changeActivePlayer(updatedState);


        if (isGameFinish(updatedState)) {
            updatedState.setFinished(true);
            setWinner(updatedState);
        }
        log.info("State output: {}", MAPPER.writeValueAsString(currentState));
        return updatedState;
    }

    private void updatePlayerResourcesStats(Board updatedState) {
        updatedState.getPlayers().stream()
                .filter(Player::isAlive)
                .forEach(player -> {
                    boolean playerHasRobots = CardUtils.isPlayerHasActiveCard(CardType.ROBOTS.getId(), player);
                    int totalRedProduction = getTotalResourceStat(player, playerHasRobots, GET_CARD_RED_PRODUCTION);
                    int totalRedConsumption = getTotalResourceStat(player, playerHasRobots, GET_CARD_RED_CONSUMPTION);
                    int totalBlueProduction = getTotalResourceStat(player, playerHasRobots, GET_CARD_BLUE_PRODUCTION);
                    int totalBlueConsumption = getTotalResourceStat(player, playerHasRobots, GET_CARD_BLUE_CONSUMPTION);

                    player.setRedProduction(totalRedProduction);
                    player.setRedConsumption(totalRedConsumption);
                    player.setBlueProduction(totalBlueProduction);
                    player.setBlueConsumption(totalBlueConsumption);

                    if (!updatedState.getGlobalPlayer().getActiveCards().isEmpty()) {
                        updatedState.getGlobalPlayer().getActiveCards().forEach(globalCard -> {
                            CardType cardType = CardUtils.getCardTypeById(globalCard.getId());
                            player.setRedProduction(player.getRedProduction() + cardType.getRedProduction());
                            player.setRedConsumption(player.getRedConsumption() + cardType.getRedConsumption());
                            player.setBlueProduction(player.getBlueProduction() + cardType.getBlueProduction());
                            player.setBlueConsumption(player.getBlueConsumption() + cardType.getBlueConsumption());
                        });
                    }
                    updateNegativeValueWithZero(player);

                });
    }


    private int getTotalResourceStat(Player player, boolean isToPlayerHasRobots, BiFunction<Card, Boolean, Integer> calculateFunction) {
        return player.getActiveCards()
                .stream()
                .filter(Card::isActive)
                .filter(card -> !card.isOneRound())
                .mapToInt(card -> calculateFunction.apply(card, isToPlayerHasRobots))
                .sum();
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

    private GameAction getAction(String name) {
        return actions.get(name);
    }

    private boolean isGameFinish(Board state) {
        long alivePlayerCount = alivePlayerCount(state);
        boolean noAvailCard = state.getPlayers().stream()
                .filter(Player::isAlive)
                .allMatch(player -> player.getAvailableCards().isEmpty());
        return alivePlayerCount <= 1 || noAvailCard;
    }

    private void setWinner(Board state) {
        if (alivePlayerCount(state) > 1) {
            state.getPlayers().stream()
                .filter(Player::isAlive)
                .max((p1, p2) -> {
                    int blueToRedCoefficientPlayerOne = CardUtils.getBlueToRedConvertationCoefficient(p1);
                    int blueToRedCoefficientPlayerTwo = CardUtils.getBlueToRedConvertationCoefficient(p2);

                    int pTotalOne = p1.getRedAmount() + p1.getBlueAmount() / blueToRedCoefficientPlayerOne;
                    int pTotalTwo = p2.getRedAmount() + p2.getBlueAmount() / blueToRedCoefficientPlayerTwo;
                    return Integer.compare(pTotalOne, pTotalTwo);
                })
                .ifPresent( player -> state.setWinner(player.getName()));
            return;
        }
        Optional<Player> optPlayer = state.getPlayers().stream()
                .filter(Player::isAlive)
                .findFirst();
        if (optPlayer.isPresent()) {
            state.setWinner(optPlayer.get().getName());
        } else {
            state.setWinner("Никто");
        }
    }

    private long alivePlayerCount(Board state) {
        return state.getPlayers().stream()
                .filter(Player::isAlive)
                .count();
    }

    private void changeActivePlayer(Board state) {
        if (GameAction.START_GAME_COMPLETED.equals(state.getAction().getName())) {
            return;
        }

        Player currentPlayer = state.fetchActivePlayer();

        if (Objects.isNull(currentPlayer)) {
            log.info("Active player is not found");
            return;
        }
        // TODO
        List<Player> players = state.getPlayers();

        List<Player> activePlayer = players.stream()
            .filter(p -> p.isAlive() || p.isActiveTurn())
            .collect(Collectors.toList());

        currentPlayer.setActiveTurn(false);
        for (int i = 0; i < activePlayer.size(); i++) {
            if (activePlayer.get(i).getName().equals(currentPlayer.getName())) {
                if (i == activePlayer.size() - 1) {
                    activePlayer.get(0).setActiveTurn(true);
                } else {
                    activePlayer.get(i + 1).setActiveTurn(true);
                }
                break;
            }
        }
    }
}
