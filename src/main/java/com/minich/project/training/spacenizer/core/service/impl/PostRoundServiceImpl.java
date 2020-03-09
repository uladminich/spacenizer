package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.PostRoundService;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PostRoundServiceImpl implements PostRoundService {

    @Override
    public boolean isRoundFinish(Board currentState) {
        return GameAction.PLAY_CARD_FINISHED.equals(currentState.getAction().getName())
                && currentState.getTurnPerRound().get() >= currentState.countActivePlayers();
    }

    @Override
    public void updatePlayerRedAmountStored(Board state) {
        state.getPlayers().forEach(player -> {
            int increaseAmount = player.getRedProduction();
            int decreaseAmount = player.getRedConsumption();
            int totalAmount = state.getRedResourceCount();

            int amountToAdd;
            if (totalAmount - increaseAmount >= 0) {
                amountToAdd = increaseAmount - decreaseAmount;
                state.setRedResourceCount(totalAmount - increaseAmount);
            } else {
                state.setRedResourceCount(0);
                amountToAdd = totalAmount - decreaseAmount;
            }

            player.setRedAmount(player.getRedAmount() + amountToAdd);

            if (player.getRedAmount() < 0) {
                player.setAlive(false);
                player.setRedConsumption(0);
                player.setRedProduction(0);
            }
        });
    }

    @Override
    public void resetTurnsPerRound(Board state) {
        state.setTurnPerRound(new AtomicInteger(0));
    }
}
