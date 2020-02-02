package com.minich.project.training.spacenizer.core.service;

import com.minich.project.training.spacenizer.model.Board;

public interface GameManager {

    Board doAction(String currentAction, Board currentState);
}
