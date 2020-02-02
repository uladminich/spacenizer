package com.minich.project.training.spacenizer.core.service.impl;

import com.minich.project.training.spacenizer.core.service.GameManager;
import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        return action != null ? action.doAction(currentState) : currentState;
    }

    private GameAction getAction(String name) {
        switch (name) {
            case GameAction.START_GAME:
                return startGameAction;
            case GameAction.PLAY_CARD:
                return playCardAction;
            default:
                return null;
        }
    }
}
