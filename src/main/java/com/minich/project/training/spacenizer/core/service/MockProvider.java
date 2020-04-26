package com.minich.project.training.spacenizer.core.service;

import com.google.common.collect.ImmutableList;
import com.minich.project.training.spacenizer.model.Board;

import java.util.List;

public interface MockProvider {
    String MOCK_GAME_ID_PLAYERS_4 = "mock-game-player-4";
    List<String> MOCK_GAME_IDS = ImmutableList.of(MOCK_GAME_ID_PLAYERS_4);
    String MOCK_BASE_PATH = "classpath:mock/%s.json";

    boolean isMockId(String gameId);

    Board getMockGame(String gameId);

}
