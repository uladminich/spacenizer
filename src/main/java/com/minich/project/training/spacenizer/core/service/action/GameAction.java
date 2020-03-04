package com.minich.project.training.spacenizer.core.service.action;

import com.minich.project.training.spacenizer.model.Board;

public interface GameAction {
    String START_GAME = "start";
    String START_GAME_COMPLETED = "start_completed";
    String PLAY_CARD = "play_card";
    String PLAY_CARD_FINISHED = "play_card_finished";

    Board doAction(Board state);

}
