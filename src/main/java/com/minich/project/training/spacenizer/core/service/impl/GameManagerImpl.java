package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameManagerImpl implements GameManager {

    @Autowired
    private PostRoundServiceImpl postRoundService;

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

        if (GameAction.PLAY_CARD_FINISHED.equals(updatedState.getAction().getName())) {
            updatedState.getTurnPerRound().incrementAndGet();
        }

        if (postRoundService.isRoundFinish(updatedState)) {
            postRoundService.updatePlayerRedAmountStored(updatedState);
            postRoundService.resetTurnsPerRound(updatedState);
        }

        changeActivePlayer(updatedState);


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

    private void changeActivePlayer(Board state) {
        if (GameAction.START_GAME_COMPLETED.equals(state.getAction().getName())) {
            return;
        }

        Player currentPlayer = state.fetchActivePlayer();

        if (Objects.isNull(currentPlayer)) {
            log.debug("Active player is not found");
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
