package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class GameManagerImpl implements GameManager {

    @Autowired
    @Qualifier(GameAction.START_GAME)
    private GameAction startGameAction;

    @Autowired
    @Qualifier(GameAction.PLAY_CARD)
    private GameAction playCardAction;

    @Override
    public Board doAction(String currentAction, Board currentState) {
        GameAction action = getAction(currentAction);
        Board updatedState = action != null ? action.doAction(currentState) : currentState;
        if (isRoundFinish(currentAction, updatedState)) {
            updatePlayerRedAmountStored(updatedState);
        }
        if (isGameFinish(updatedState)) {
            updatedState.setFinished(true);
            setWinner(updatedState);
        }
        return updatedState;
    }

    private GameAction getAction(String name) {
        //TODO rewrite with map of injects if possible
        switch (name) {
            case GameAction.START_GAME:
                return startGameAction;
            case GameAction.PLAY_CARD:
                return playCardAction;
            default:
                return null;
        }
    }

    private boolean isRoundFinish(String currentAction, Board currentState) {
        return GameAction.PLAY_CARD.equals(currentAction)
                && currentState.getFirstPlayerId().equals(currentState.fetchActivePlayer().getName());
    }

    private void updatePlayerRedAmountStored(Board state) {
        state.getPlayers().forEach(player -> {
            int increaseAmount = player.getRedProduction();
            int decreaseAmount = player.getRedConsumption();
            int totalAmount = state.getRedResourceCount();

            if (totalAmount - increaseAmount >= 0) {
                state.setRedResourceCount(totalAmount - increaseAmount);
            } else {
                state.setRedResourceCount(0);
            }
            player.setRedAmount(player.getRedAmount() + (increaseAmount - decreaseAmount));

            if (player.getRedAmount() < 0) {
                player.setAlive(false);
            }
        });
    }

    private boolean isGameFinish(Board state) {
        long alivePlayerCount = alivePlayerCount(state);
        boolean noAvailCard = state.getPlayers().stream().allMatch(player -> player.getAvailableCards().isEmpty());
        return alivePlayerCount <= 1 || noAvailCard;
    }

    private void setWinner(Board state) {
        if (alivePlayerCount(state) > 1) {
            state.getPlayers().stream()
                .filter(Player::isAlive)
                .max(Comparator.comparing(Player::getRedAmount))
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
}
