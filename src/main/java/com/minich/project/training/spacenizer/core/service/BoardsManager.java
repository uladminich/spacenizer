package com.minich.project.training.spacenizer.core.service;

import com.minich.project.training.spacenizer.model.Board;

public interface BoardsManager {

    Board getOrCreateBoard(String gameId, String userName);

    void addBoard(String gameId, Board board);

    boolean isBoardPresent(String gameId);
}
