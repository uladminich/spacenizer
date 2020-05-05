package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.PostRoundService;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import org.springframework.stereotype.Service;

@Service
public class PostRoundServiceImpl implements PostRoundService {
    private static final int DEFAULT_BLUE_TO_RED_CONVERT_COEFF = 2;

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

    // TODO updateRedResource and updateBlueResource are similar, probably make sense to refactor in more pretty way
    private void updateRedResource(Board state, Player player) {
        int increaseRedAmount = player.getRedProduction();
        int decreaseRedAmount = player.getRedConsumption();
        int totalRedAmount = state.getRedResourceCount();

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
            if (player.getBlueAmount() >= DEFAULT_BLUE_TO_RED_CONVERT_COEFF) {
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
        int blueToRed = totalAbsRedAmount * DEFAULT_BLUE_TO_RED_CONVERT_COEFF;
        if (blueToRed > totalBlueAmount) {
            blueToRed = totalBlueAmount - totalBlueAmount % DEFAULT_BLUE_TO_RED_CONVERT_COEFF;
            isAlive =  false;
        }
        player.setRedAmount(player.getRedAmount() + totalAbsRedAmount);
        player.setBlueAmount(totalBlueAmount - blueToRed);
        return isAlive;
    }
}
