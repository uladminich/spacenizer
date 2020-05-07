package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.PostRoundService;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;
import com.minich.project.training.spacenizer.model.cards.CardType;
import com.minich.project.training.spacenizer.utils.CardUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PostRoundServiceImpl implements PostRoundService {
    private static final Random RANDOM = new Random();

    @Override
    public boolean isRoundFinish(Board currentState) {
        return (GameAction.PLAY_CARD_FINISHED.equals(currentState.getAction().getName())
                || GameAction.CHANGE_CARD_FINISHED.equals(currentState.getAction().getName())
                || GameAction.SKIP_TURN_FINISHED.equals(currentState.getAction().getName()))
                && currentState.getTurnPerRound() >= currentState.countActivePlayers();
    }

    @Override
    public void updatePlayerResourceAmountStored(Board state) {
        state.getPlayers().stream()
                .filter(Player::isAlive)
                .forEach(player -> {
                    updateRedResource(state, player);
                    updateBlueResource(state, player);
                    updateAliveStatus(player);
                });
    }

    @Override
    public void resetTurnsPerRound(Board state) {
        state.setTurnPerRound(0);
    }

    @Override
    public void applySpecialGlobalCardAction(Board state) {
        List<Card> activeCards = state.getGlobalPlayer().getActiveCards();
        if (hasEarthquakesCard(activeCards)) {
            if (RANDOM.nextInt(2) == 1) { // 50% chance to destroy
                List<Card> activeBuildingCards = state.getPlayers()
                        .stream()
                        .filter(Player::isAlive)
                        .flatMap(player -> player.getActiveCards().stream())
                        .filter(card -> CardUtils.isBuildingCard(card.getId()))
                        .collect(Collectors.toList());

                int destroyAmount = getNumberOfBuildingToDestroy(state);
                state.getAction().setDescription("Землетресение:\n\r");
                for (int i = 0; i < destroyAmount; i++) {
                    int cardToRemoveIndex = RANDOM.nextInt(activeBuildingCards.size());
                    Card card = activeBuildingCards.get(cardToRemoveIndex);
                    state.getPlayers()
                            .stream()
                            .filter(Player::isAlive)
                            .filter(p -> playerHasCardWithId(p, card.getId()))
                            .findAny().ifPresent(player -> {
                                player.getActiveCards().remove(card);
                                state.getAction().setDescription(state.getAction().getDescription() + " разрушена постройка '" + card.getTitle() + "' у игрока [" + player.getName() + "]\n\r");

                    });
                    activeBuildingCards.remove(card);
                }
            } else if (!GameAction.START_GAME_COMPLETED.equals(state.getAction().getName())) {
                state.getAction().setDescription("Землетресение: построек не разрушено.");
            }
        }
    }

    private int getNumberOfBuildingToDestroy(Board state) {
        int activePlayerAmount = (int) state.getPlayers().stream().filter(Player::isAlive).count();
        int randomIntLessNine = RANDOM.nextInt(10);
        /*
            Random number is in range [0-9].
            3: random number is [8 or 9] and 3+ active player
            2:(random number is [5, 6, 7, 8 or 9] and 2+ active player) or random number is [8 or 9] and less that 3 player
            1: any other cases
         */
        if (randomIntLessNine >= 8 && activePlayerAmount > 2) { //
            return  3;
        } else if ((randomIntLessNine > 4 && activePlayerAmount >= 2) || randomIntLessNine >= 8) {
            return  2;
        }
        return 1;
    }

    private boolean playerHasCardWithId(Player player, long id) {
        return player.getActiveCards().stream()
                .anyMatch(card -> card.getId() == id);
    }
    private boolean hasEarthquakesCard(List<Card> activeCards) {
        return activeCards.stream()
                .anyMatch(card -> card.getId() == CardType.EARTHQUAKES.getId());
    }

    // TODO updateRedResource and updateBlueResource are similar, probably make sense to refactor in more pretty way
    private void updateRedResource(Board state, Player player) {
        int increaseRedAmount = player.getRedProduction();
        int decreaseRedAmount = player.getRedConsumption();
        int totalRedAmount = state.getRedResourceCount();
        if (decreaseRedAmount < 0) {
            decreaseRedAmount = 0;
        }
        int redAmountToAdd;
        if (totalRedAmount - increaseRedAmount >= 0) {
            redAmountToAdd = increaseRedAmount - decreaseRedAmount;
            state.setRedResourceCount(totalRedAmount - increaseRedAmount);
        } else {
            state.setRedResourceCount(0);
            redAmountToAdd = totalRedAmount - decreaseRedAmount;
        }

        player.setRedAmount(player.getRedAmount() + redAmountToAdd);
    }

    private void updateBlueResource(Board state, Player player) {
        int increaseBlueAmount = player.getBlueProduction();
        int decreaseBlueAmount = player.getBlueConsumption();
        int totalBlueAmount = state.getBlueResourceCount();
        if (decreaseBlueAmount < 0) {
            decreaseBlueAmount = 0;
        }
        int blueAmountToAdd;
        if (totalBlueAmount - increaseBlueAmount >= 0) {
            blueAmountToAdd = increaseBlueAmount - decreaseBlueAmount;
            state.setBlueResourceCount(totalBlueAmount - increaseBlueAmount);
        } else {
            state.setBlueResourceCount(0);
            blueAmountToAdd = totalBlueAmount - decreaseBlueAmount;
        }

        player.setBlueAmount(player.getBlueAmount() + blueAmountToAdd);
        if (player.getBlueAmount() < 0) {
            player.setBlueAmount(0);
        }
    }

    private void updateAliveStatus(Player player) {
        if (player.getRedAmount() < 0) {
            boolean isAlive = false;
            int minAmountBlueToConvert = CardUtils.getBlueToRedConvertationCoefficient(player);
            if (player.getBlueAmount() >= minAmountBlueToConvert) {
                isAlive = convertRedToBlue(player);
            }

            if (!isAlive) {
                player.setAlive(false);
                player.setRedConsumption(0);
                player.setRedProduction(0);
                player.setBlueConsumption(0);
                player.setBlueProduction(0);
            }
        }
    }

    private boolean convertRedToBlue(Player player) {
        int totalAbsRedAmount = Math.abs(player.getRedAmount());
        int totalBlueAmount = player.getBlueAmount();
        boolean isAlive = true;
        int blueToRedCoefficient = CardUtils.getBlueToRedConvertationCoefficient(player);
        int blueToRed = totalAbsRedAmount * blueToRedCoefficient;
        if (blueToRed > totalBlueAmount) {
            blueToRed = totalBlueAmount - totalBlueAmount % blueToRedCoefficient;
            isAlive =  false;
        }
        player.setRedAmount(player.getRedAmount() + totalAbsRedAmount);
        player.setBlueAmount(totalBlueAmount - blueToRed);
        return isAlive;
    }
}
