package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import org.springframework.stereotype.Service;

@Service(GameAction.SKIP_TURN)
public class SkipTurnActionImpl implements GameAction {

    @Override
    public Board doAction(Board state) {
        state.getAction().setName(GameAction.SKIP_TURN_FINISHED);
        return state;
    }
}
