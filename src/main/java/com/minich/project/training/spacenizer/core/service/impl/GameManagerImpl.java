package com.minich.project.training.spacenizer.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameManagerImpl implements GameManager {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private PostRoundServiceImpl postRoundService;

    @Autowired
    @Qualifier(GameAction.START_GAME)
    private GameAction startGameAction;

    @Autowired
    @Qualifier(GameAction.PLAY_CARD)
    private GameAction playCardAction;

    @Autowired
    @Qualifier(GameAction.CHANGE_CARD)
    private GameAction changeCardAction;

    @Autowired
    @Qualifier(GameAction.SKIP_TURN)
    private GameAction skipTurnAction;

    @SneakyThrows
    @Override
    public Board doAction(String currentAction, Board currentState) {
        log.info("State: {}", MAPPER.writeValueAsString(currentState));

        GameAction action = getAction(currentAction);
        Board updatedState = action != null ? action.doAction(currentState) : currentState;

        String updatedAction = updatedState.getAction().getName();
        if (GameAction.PLAY_CARD_FINISHED.equals(updatedAction)
            || GameAction.CHANGE_CARD_FINISHED.equals(updatedAction)
            || GameAction.SKIP_TURN_FINISHED.equals(updatedAction)) {
            updatedState.setTurnPerRound(updatedState.getTurnPerRound() + 1);
        }

        if (postRoundService.isRoundFinish(updatedState)) {
            postRoundService.updatePlayerResourceAmountStored(updatedState);
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
            case GameAction.CHANGE_CARD:
                return changeCardAction;
            case GameAction.SKIP_TURN:
                return skipTurnAction;
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
                .max((p1, p2) -> {
                    int pTotalOne = p1.getRedAmount() + p1.getBlueAmount() /2;
                    int pTotalTwo = p2.getRedAmount() + p2.getBlueAmount() /2;
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
