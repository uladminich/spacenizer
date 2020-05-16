package com.minich.project.training.spacenizer.core.service.action.impl;

import com.minich.project.training.spacenizer.core.service.action.GameAction;
import com.minich.project.training.spacenizer.model.Board;
import com.minich.project.training.spacenizer.model.Player;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service(GameAction.SKIP_TURN)
public class SkipTurnActionImpl implements GameAction {

    private static final String MESSAGE_FORMAT = "[%s] пропустил ход.";

    @Override
    public Board doAction(Board state) {
        state.getPlayers().stream()
                .filter(Player::isActiveTurn)
                .findFirst()
                .ifPresent(player -> {
                    state.getAction().setPlayerActionDescription(String.format(MESSAGE_FORMAT, player.getName()));
                    state.getAction().setDescription(StringUtils.EMPTY);
                });
        state.getAction().setName(GameAction.SKIP_TURN_FINISHED);
        return state;
    }
}
