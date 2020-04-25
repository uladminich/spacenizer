package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minich.project.training.spacenizer.model.cards.Card;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board implements Serializable {
    private String boardId;
    private List<Player> players;
    private Action action;
    // main energy resource if no - player lose (except the case with blue)
    private int redResourceCount;
    // blue - used for some card for action, used when no red resource, 2 blue = 1 red by default
    private int blueResourceCount;
    private String firstPlayerId;
    private boolean isFinished;
    private String winner;
    private int turnPerRound = 0;
    private Player globalPlayer;

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
