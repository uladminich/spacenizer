package com.minich.project.training.spacenizer.core.service;

import com.minich.project.training.spacenizer.model.Board;

public interface PostRoundService {

    boolean isRoundFinish(Board currentState);

    void updatePlayerResourceAmountStored(Board state);

    void resetTurnsPerRound(Board state);

    void applySpecialGlobalCardAction(Board state);
}
