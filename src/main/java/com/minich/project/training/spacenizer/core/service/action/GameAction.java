package com.minich.project.training.spacenizer.core.service.action;

import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import com.minich.project.training.spacenizer.model.cards.Card;

import java.util.List;

public interface GameAction {
    String START_GAME = "start";
    String START_GAME_COMPLETED = "start_completed";
    String PLAY_CARD = "play_card";
    String PLAY_CARD_FINISHED = "play_card_finished";
    String CHANGE_CARD = "change_card";
    String CHANGE_CARD_FINISHED = "change_card_finished";
    String DASH = "-";

    Board doAction(Board state);

    default Player getPlayerById(String id, List<Player> players) {
        return players.stream()
                .filter(p -> id.equals(p.getName()))
                .findFirst()
                .orElse(null);
    }

    default Card getAvailableCardById(String idUI, Player player) {
        return player.getAvailableCards()
                .stream()
                .filter(c -> c.getIdUI().equals(idUI))
                .findFirst()
                .orElse(null);
    }

}
