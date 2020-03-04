package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board implements Serializable {
    private String boardId;
    private List<Player> players;
    private Action action;
    private int redResourceCount;
    private String firstPlayerId;
    private boolean isFinished;
    private String winner;
    private AtomicInteger turnPerRound = new AtomicInteger(0);

    public void addPlayer(Player player) {
        if (players != null && player != null && player.getBoardId().equals(boardId)) {
            players.add(player);
        }
    }

    public Player fetchActivePlayer() {
        return players.stream().filter(Player::isActiveTurn).findFirst().orElse(null);
    }

    public long countActivePlayers(){
        return players.stream().filter(Player::isAlive).count();
    }
}
