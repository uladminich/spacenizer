package com.minich.project.training.spacenizer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String action;
    private int redResourceCount;

    public void addPlayer(Player player) {
        if (players != null && player != null && player.getBoardId().equals(boardId)) {
            players.add(player);
        }
    }
}
